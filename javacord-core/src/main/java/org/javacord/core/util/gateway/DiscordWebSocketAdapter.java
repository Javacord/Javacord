package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.javacord.api.Javacord;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.connection.LostConnectionEvent;
import org.javacord.api.event.connection.ReconnectEvent;
import org.javacord.api.event.connection.ResumeEvent;
import org.javacord.api.listener.connection.LostConnectionListener;
import org.javacord.api.listener.connection.ReconnectListener;
import org.javacord.api.listener.connection.ResumeListener;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.event.connection.LostConnectionEventImpl;
import org.javacord.core.event.connection.ReconnectEventImpl;
import org.javacord.core.event.connection.ResumeEventImpl;
import org.javacord.core.util.concurrent.ThreadFactory;
import org.javacord.core.util.handler.ReadyHandler;
import org.javacord.core.util.handler.ResumedHandler;
import org.javacord.core.util.handler.channel.ChannelCreateHandler;
import org.javacord.core.util.handler.channel.ChannelDeleteHandler;
import org.javacord.core.util.handler.channel.ChannelPinsUpdateHandler;
import org.javacord.core.util.handler.channel.ChannelUpdateHandler;
import org.javacord.core.util.handler.channel.WebhooksUpdateHandler;
import org.javacord.core.util.handler.guild.GuildBanAddHandler;
import org.javacord.core.util.handler.guild.GuildBanRemoveHandler;
import org.javacord.core.util.handler.guild.GuildCreateHandler;
import org.javacord.core.util.handler.guild.GuildDeleteHandler;
import org.javacord.core.util.handler.guild.GuildEmojisUpdateHandler;
import org.javacord.core.util.handler.guild.GuildMemberAddHandler;
import org.javacord.core.util.handler.guild.GuildMemberRemoveHandler;
import org.javacord.core.util.handler.guild.GuildMemberUpdateHandler;
import org.javacord.core.util.handler.guild.GuildMembersChunkHandler;
import org.javacord.core.util.handler.guild.GuildUpdateHandler;
import org.javacord.core.util.handler.guild.VoiceStateUpdateHandler;
import org.javacord.core.util.handler.guild.role.GuildRoleCreateHandler;
import org.javacord.core.util.handler.guild.role.GuildRoleDeleteHandler;
import org.javacord.core.util.handler.guild.role.GuildRoleUpdateHandler;
import org.javacord.core.util.handler.message.MessageCreateHandler;
import org.javacord.core.util.handler.message.MessageDeleteBulkHandler;
import org.javacord.core.util.handler.message.MessageDeleteHandler;
import org.javacord.core.util.handler.message.MessageUpdateHandler;
import org.javacord.core.util.handler.message.reaction.MessageReactionAddHandler;
import org.javacord.core.util.handler.message.reaction.MessageReactionRemoveAllHandler;
import org.javacord.core.util.handler.message.reaction.MessageReactionRemoveHandler;
import org.javacord.core.util.handler.user.PresenceUpdateHandler;
import org.javacord.core.util.handler.user.PresencesReplaceHandler;
import org.javacord.core.util.handler.user.TypingStartHandler;
import org.javacord.core.util.handler.user.UserUpdateHandler;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The main websocket adapter.
 */
public class DiscordWebSocketAdapter extends WebSocketListener {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordWebSocketAdapter.class);

    private static String gateway;
    private static final ReadWriteLock gatewayLock = new ReentrantReadWriteLock();
    private static final Lock gatewayReadLock = gatewayLock.readLock();
    private static final Lock gatewayWriteLock = gatewayLock.writeLock();

    private final DiscordApiImpl api;
    private final HashMap<String, PacketHandler> handlers = new HashMap<>();
    private final CompletableFuture<Boolean> ready = new CompletableFuture<>();

    private final AtomicReference<WebSocket> websocket = new AtomicReference<>();

    private final AtomicReference<Future<?>> heartbeatTimer = new AtomicReference<>();
    private final AtomicBoolean heartbeatAckReceived = new AtomicBoolean();

    private volatile int lastSeq = -1;
    private volatile String sessionId = null;

    private volatile boolean reconnect = true;

    private final AtomicBoolean lastFrameWasIdentify = new AtomicBoolean();

    private volatile long lastGuildMembersChunkReceived = System.currentTimeMillis();

    // A reconnect attempt counter
    private final AtomicInteger reconnectAttempt = new AtomicInteger();

    // A queue which contains server ids for the "request guild members" packet
    private final BlockingQueue<Long> requestGuildMembersQueue = new LinkedBlockingQueue<>();

    private static final Map<String, Long> lastIdentificationPerAccount = Collections.synchronizedMap(new HashMap<>());
    private static final ConcurrentMap<String, Semaphore> connectionDelaySemaphorePerAccount =
            new ConcurrentHashMap<>();

    static {
        // This scheduler makes sure that the semaphores get released after a while if it failed in the listener
        // for whatever reason. It's just a fail-safe.
        Executors.newSingleThreadScheduledExecutor(
                new ThreadFactory("Javacord - Connection Delay Semaphores Starvation Protector", true)
        ).scheduleWithFixedDelay(() -> {
            try {
                connectionDelaySemaphorePerAccount.forEach((token, semaphore) -> {
                    if ((semaphore.availablePermits() == 0) &&
                        (System.currentTimeMillis() - lastIdentificationPerAccount.getOrDefault(token, 0L) >= 15000)) {
                        semaphore.release();
                    }
                });
            } catch (Throwable t) {
                logger.error("Failed to do the backup semaphore releasing!", t);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * Creates a new discord websocket adapter.
     *
     * @param api The discord api instance.
     */
    public DiscordWebSocketAdapter(DiscordApiImpl api) {
        this.api = api;

        registerHandlers();
        connect();

        ExecutorService requestGuildMembersQueueConsumer =
                api.getThreadPool().getSingleDaemonThreadExecutorService("Request Server Members Queue Consumer");
        requestGuildMembersQueueConsumer.submit(() -> {
            while (!requestGuildMembersQueueConsumer.isShutdown()) {
                try {
                    // wait 1 minute for a request being queued
                    Long nextServerId = requestGuildMembersQueue.poll(1, TimeUnit.MINUTES);
                    // timed out => check whether the abort condition triggers
                    if (nextServerId == null) {
                        continue;
                    }
                    // put the element back to the queue
                    requestGuildMembersQueue.add(nextServerId);
                    // send requests in up-to 50 guilds batches
                    AtomicInteger batchCounter = new AtomicInteger();
                    requestGuildMembersQueue.stream().distinct()
                            .collect(Collectors.groupingBy(serverId -> batchCounter.getAndIncrement() / 50))
                            .values()
                            .forEach(serverIdBatch -> {
                                requestGuildMembersQueue.removeAll(serverIdBatch);
                                ObjectNode requestGuildMembersPacket = JsonNodeFactory.instance.objectNode()
                                        .put("op", GatewayOpcode.REQUEST_GUILD_MEMBERS.getCode());
                                ObjectNode data = requestGuildMembersPacket.putObject("d")
                                        .put("query", "")
                                        .put("limit", 0);
                                if (serverIdBatch.size() == 1) {
                                    data.put("guild_id", Long.toUnsignedString(serverIdBatch.get(0)));
                                } else {
                                    ArrayNode guildIds = data.putArray("guild_id");
                                    serverIdBatch.stream()
                                            .map(Long::toUnsignedString)
                                            .forEach(guildIds::add);
                                }
                                logger.debug("Sending request guild members packet {}",
                                             requestGuildMembersPacket.toString());
                                getWebSocket().send(requestGuildMembersPacket.toString());
                            });
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                } catch (Throwable t) {
                    logger.error("Failed to process request guild members queue!", t);
                }
            }
        });
    }

    /**
     * Gets the gateway used to connect.
     * If no gateway was requested or set so far, it will request one from Discord.
     *
     * @param api The api used to make the rest call.
     * @return The gateway url as string.
     */
    private static String getGateway(DiscordApiImpl api) {
        gatewayReadLock.lock();
        if (gateway == null) {
            gatewayReadLock.unlock();
            gatewayWriteLock.lock();
            try {
                if (gateway == null) {
                    gateway = new RestRequest<String>(api, RestMethod.GET, RestEndpoint.GATEWAY)
                            .includeAuthorizationHeader(false)
                            .execute(result -> result.getJsonBody().get("url").asText())
                            .join();
                }
                gatewayReadLock.lock();
            } finally {
                gatewayWriteLock.unlock();
            }
        }

        try {
            return gateway;
        } finally {
            gatewayReadLock.unlock();
        }
    }

    /**
     * Sets the gateway used to connect.
     *
     * @param gateway The gateway to set.
     */
    public static void setGateway(String gateway) {
        gatewayWriteLock.lock();
        try {
            DiscordWebSocketAdapter.gateway = gateway;
        } finally {
            gatewayWriteLock.unlock();
        }
    }

    /**
     * Disconnects from the websocket.
     */
    public void disconnect() {
        reconnect = false;
        //TODO: Add a reason to this close
        websocket.get().close(WebSocketCloseReason.DISCONNECT.getNumericCloseCode(), "");
        // cancel heartbeat timer if within one minute no disconnect event was dispatched
        api.getThreadPool().getDaemonScheduler().schedule(() -> heartbeatTimer.updateAndGet(future -> {
            if (future != null) {
                future.cancel(false);
            }
            return null;
        }), 1, TimeUnit.MINUTES);
    }

    /**
     * Connects the websocket.
     */
    private void connect() {
        try {
            Request webSocketRequest = new Request.Builder()
                    .url(getGateway(api) + "?encoding=json&v=" + Javacord.DISCORD_GATEWAY_VERSION)
                    .addHeader("Accept-Encoding", "gzip")
                    .build();
            waitForIdentifyRateLimit();
            WebSocket webSocket = api.getHttpClient().newWebSocket(webSocketRequest, this);
            this.websocket.set(webSocket);
        } catch (Throwable t) {
            logger.warn("An error occurred while connecting to websocket", t);
            if (reconnect) {
                reconnectAttempt.incrementAndGet();
                logger.info("Retrying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt.get()));
                // Reconnect after a (short?) delay depending on the amount of reconnect attempts
                api.getThreadPool().getScheduler()
                        .schedule(() -> {
                            gatewayWriteLock.lock();
                            try {
                                gateway = null;
                            } finally {
                                gatewayWriteLock.unlock();
                            }
                            this.connect();
                        }, api.getReconnectDelay(reconnectAttempt.get()), TimeUnit.SECONDS);
            }
        }
    }

    /**
     * Identification is rate limited to once every 5 seconds,
     * so don't try to more often per account, even in different instances.
     * This method waits for the identification rate limit to be over, then returns.
     */
    private void waitForIdentifyRateLimit() {
        String token = api.getToken();
        connectionDelaySemaphorePerAccount.computeIfAbsent(token, key -> new Semaphore(1)).acquireUninterruptibly();
        for (long delay = 5100 - (System.currentTimeMillis() - lastIdentificationPerAccount.getOrDefault(token, 0L));
             delay > 0;
             delay = 5100 - (System.currentTimeMillis() - lastIdentificationPerAccount.getOrDefault(token, 0L))) {
            logger.debug("Delaying connecting by {}ms", delay);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignored) { }
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        if (logger.isTraceEnabled()) {
            logger.trace("onOpen: response='{}'", response.toString());
        }
    }

    @Override
    public void onMessage(WebSocket websocket, String text) {
        if(logger.isTraceEnabled()) {
            logger.trace("onMessage: text='{}'", text);
        }
        ObjectMapper mapper = api.getObjectMapper();
        JsonNode packet;
        try {
            packet = mapper.readTree(text);
        } catch (IOException e) {
            logger.error("Unable to read json tree!", e);
            return;
        }

        int op = packet.get("op").asInt();
        Optional<GatewayOpcode> opcode = GatewayOpcode.fromCode(op);
        if (!opcode.isPresent()) {
            logger.debug("Received unknown packet (op: {}, content: {})", op, packet.toString());
            return;
        }

        switch (opcode.get()) {
            case DISPATCH:
                lastSeq = packet.get("s").asInt();
                String type = packet.get("t").asText();
                PacketHandler handler = handlers.get(type);
                if (handler != null) {
                    handler.handlePacket(packet.get("d"));
                } else {
                    logger.debug("Received unknown packet of type {} (packet: {})", type, packet.toString());
                }

                if (type.equals("GUILD_MEMBERS_CHUNK")) {
                    lastGuildMembersChunkReceived = System.currentTimeMillis();
                }
                if (type.equals("RESUMED")) {
                    reconnectAttempt.set(0);
                    logger.debug("Received RESUMED packet");

                    ResumeEvent resumeEvent = new ResumeEventImpl(api);
                    List<ResumeListener> listeners = new ArrayList<>(api.getResumeListeners());
                    api.getEventDispatcher()
                            .dispatchEvent(api, listeners, listener -> listener.onResume(resumeEvent));
                }
                if (type.equals("READY")) {
                    reconnectAttempt.set(0);
                    sessionId = packet.get("d").get("session_id").asText();
                    // Discord sends us GUILD_CREATE packets after logging in. We will wait for them.
                    api.getThreadPool().getSingleThreadExecutorService("Startup Servers Wait Thread").submit(() -> {
                        boolean allUsersLoaded = false;
                        boolean allServersLoaded = false;
                        int lastUnavailableServerAmount = 0;
                        int sameUnavailableServerCounter = 0;
                        while (api.isWaitingForServersOnStartup() && (!allServersLoaded || !allUsersLoaded)) {
                            if (api.getUnavailableServers().size() == lastUnavailableServerAmount) {
                                sameUnavailableServerCounter++;
                            } else {
                                lastUnavailableServerAmount = api.getUnavailableServers().size();
                                sameUnavailableServerCounter = 0;
                            }
                            allServersLoaded = api.getUnavailableServers().isEmpty();
                            if (allServersLoaded) {
                                allUsersLoaded = api.getAllServers().stream()
                                        .noneMatch(server -> server.getMemberCount() != server.getMembers().size());
                            }
                            if (sameUnavailableServerCounter > 20
                                    && lastGuildMembersChunkReceived + 5000 < System.currentTimeMillis()) {
                                // It has been more than two seconds since no more servers became available and more
                                // than five seconds since the last guild member chunk event was received. We
                                // can assume that this will not change anytime soon, most likely because Discord
                                // itself has some issues. Let's break the loop!
                                break;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ignored) { }
                        }
                        ReconnectEvent reconnectEvent = new ReconnectEventImpl(api);
                        List<ReconnectListener> listeners = new ArrayList<>(api.getReconnectListeners());
                        api.getEventDispatcher()
                                .dispatchEvent(api, listeners, listener -> listener.onReconnect(reconnectEvent));
                        ready.complete(true);
                    });
                    logger.debug("Received READY packet");
                }
                break;
            case HEARTBEAT:
                sendHeartbeat(websocket);
                break;
            case RECONNECT:
                websocket.close(WebSocketCloseReason.COMMANDED_RECONNECT.getNumericCloseCode(),
                                    WebSocketCloseReason.COMMANDED_RECONNECT.getCloseReason());
                break;
            case INVALID_SESSION:
                long fakeLastIdentificationTime = System.currentTimeMillis();
                if (lastFrameWasIdentify.get()) {
                    logger.info("Hit identifying rate limit. Retrying in 5 seconds...");
                } else {
                    // Invalid session :(
                    int zeroToFourSeconds = (int) (Math.random() * 4000);
                    logger.info("Could not resume session. Reconnecting in {}.{} seconds...",
                                1 + zeroToFourSeconds / 1000,
                                1 + zeroToFourSeconds / 100 % 10);
                    fakeLastIdentificationTime -= 4000 - zeroToFourSeconds;
                }
                lastIdentificationPerAccount.put(api.getToken(), fakeLastIdentificationTime);
                waitForIdentifyRateLimit();
                sendIdentify(websocket);
                break;
            case HELLO:
                logger.debug("Received HELLO packet");

                JsonNode data = packet.get("d");
                int heartbeatInterval = data.get("heartbeat_interval").asInt();
                heartbeatTimer.updateAndGet(future -> {
                    if (future != null) {
                        future.cancel(false);
                    }
                    return startHeartbeat(websocket, heartbeatInterval);
                });

                if (sessionId == null) {
                    sendIdentify(websocket);
                } else {
                    connectionDelaySemaphorePerAccount.get(api.getToken()).release();
                    sendResume(websocket);
                }
                break;
            case HEARTBEAT_ACK:
                logger.debug("Heartbeat ACK received");
                heartbeatAckReceived.set(true);
                break;
            default:
                logger.debug("Received unknown packet (op: {}, content: {})", op, packet.toString());
                break;
        }
    }

    @Override
    public void onMessage(WebSocket websocket, ByteString binary) {
        if(logger.isTraceEnabled()) {
            logger.trace("onMessage: binary='{}'", binary);
        }
        Inflater decompressor = new Inflater();
        decompressor.setInput(binary.toByteArray());
        ByteArrayOutputStream bos = new ByteArrayOutputStream(binary.size());
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            int count;
            try {
                count = decompressor.inflate(buf);
            } catch (DataFormatException e) {
                logger.warn("An error occurred while decompressing data", e);
                return;
            }
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException ignored) { }
        byte[] decompressedData = bos.toByteArray();
        try {
            String message = new String(decompressedData, "UTF-8");
            logger.trace("onTextMessage: text='{}'", message);
            onMessage(websocket, message);
        } catch (UnsupportedEncodingException e) {
            logger.warn("An error occurred while decompressing data", e);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason){
        if(logger.isTraceEnabled()) {
            logger.trace("onClosing: code='{}' reason='{}'", code, reason);
        }
    }

    @Override
    public void onClosed(WebSocket websocket, int code, String reason) {
        if(logger.isTraceEnabled()) {
            logger.trace("onClosed: code='{}' reason='{}'", code, reason);
        }

        String closeCodeString = WebSocketCloseCode.fromCode(code)
                .map(closeCode -> closeCode + " (" + code + ")")
                .orElseGet(() -> String.valueOf(code));

        logger.info("Websocket closed with reason '{}' and code {}!",
                reason, closeCodeString);

        LostConnectionEvent lostConnectionEvent = new LostConnectionEventImpl(api);
        List<LostConnectionListener> listeners = new ArrayList<>(api.getLostConnectionListeners());
        api.getEventDispatcher()
                .dispatchEvent(api, listeners, listener -> listener.onLostConnection(lostConnectionEvent));

        heartbeatTimer.updateAndGet(future -> {
            if (future != null) {
                future.cancel(false);
            }
            return null;
        });

        if (!ready.isDone()) {
            ready.complete(false);
            return;
        }

        // Reconnect
        if (reconnect) {
            reconnectAttempt.incrementAndGet();
            logger.info("Trying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt.get()));
            // Reconnect after a (short?) delay depending on the amount of reconnect attempts
            api.getThreadPool().getScheduler()
                    .schedule(this::connect, api.getReconnectDelay(reconnectAttempt.get()), TimeUnit.SECONDS);
        }
    }

    /**
     * Starts the heartbeat.
     *
     * @param websocket The websocket the heartbeat should be sent to.
     * @param heartbeatInterval The heartbeat interval.
     * @return The timer used for the heartbeat.
     */
    private Future<?> startHeartbeat(final WebSocket websocket, final int heartbeatInterval) {
        // first heartbeat should assume last heartbeat was answered properly
        heartbeatAckReceived.set(true);
        return api.getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
            try {
                if (heartbeatAckReceived.getAndSet(false)) {
                    sendHeartbeat(websocket);
                    logger.debug("Sent heartbeat (interval: {})", heartbeatInterval);
                } else {
                    websocket.close(WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getNumericCloseCode(),
                                        WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getCloseReason());
                }
            } catch (Throwable t) {
                logger.error("Failed to send heartbeat or close web socket!", t);
            }
        }, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Sends the heartbeat.
     *
     * @param websocket The websocket the heartbeat should be sent to.
     */
    private void sendHeartbeat(WebSocket websocket) {
        ObjectNode heartbeatPacket = JsonNodeFactory.instance.objectNode();
        heartbeatPacket.put("op", GatewayOpcode.HEARTBEAT.getCode());
        heartbeatPacket.put("d", lastSeq);
        websocket.send(heartbeatPacket.toString());
    }

    /**
     * Sends the resume packet.
     *
     * @param websocket The websocket the resume packet should be sent to.
     */
    private void sendResume(WebSocket websocket) {
        ObjectNode resumePacket = JsonNodeFactory.instance.objectNode()
                .put("op", GatewayOpcode.RESUME.getCode());
        resumePacket.putObject("d")
                .put("token", api.getToken())
                .put("session_id", sessionId)
                .put("seq", lastSeq);
        logger.debug("Sending resume packet");
        websocket.send(resumePacket.toString());
        this.lastFrameWasIdentify.set(false);
    }

    /**
     * Sends the identify packet.
     *
     * @param websocket The websocket the identify packet should be sent to.
     */
    private void sendIdentify(WebSocket websocket) {
        ObjectNode identifyPacket = JsonNodeFactory.instance.objectNode()
                .put("op", GatewayOpcode.IDENTIFY.getCode());
        ObjectNode data = identifyPacket.putObject("d");
        String token = api.getToken();
        data.put("token", token)
                .put("compress", true)
                .put("large_threshold", 250)
                .putObject("properties")
                .put("$os", System.getProperty("os.name"))
                .put("$browser", "Javacord")
                .put("$device", "Javacord")
                .put("$referrer", "")
                .put("$referring_domain", "");
        if (api.getTotalShards() > 1) {
            data.putArray("shard").add(api.getCurrentShard()).add(api.getTotalShards());
        }
        websocket.send(data.toString());
        lastIdentificationPerAccount.put(token, System.currentTimeMillis());
        connectionDelaySemaphorePerAccount.get(token).release();
        this.lastFrameWasIdentify.set(true);
    }

    /**
     * Registers all handlers.
     */
    private void registerHandlers() {
        // general
        addHandler(new ReadyHandler(api));
        addHandler(new ResumedHandler(api));

        // server
        addHandler(new GuildBanAddHandler(api));
        addHandler(new GuildBanRemoveHandler(api));
        addHandler(new GuildCreateHandler(api));
        addHandler(new GuildDeleteHandler(api));
        addHandler(new GuildMembersChunkHandler(api));
        addHandler(new GuildMemberAddHandler(api));
        addHandler(new GuildMemberRemoveHandler(api));
        addHandler(new GuildMemberUpdateHandler(api));
        addHandler(new GuildUpdateHandler(api));
        addHandler(new VoiceStateUpdateHandler(api));

        // role
        addHandler(new GuildRoleCreateHandler(api));
        addHandler(new GuildRoleDeleteHandler(api));
        addHandler(new GuildRoleUpdateHandler(api));

        // emoji
        addHandler(new GuildEmojisUpdateHandler(api));

        // channel
        addHandler(new ChannelCreateHandler(api));
        addHandler(new ChannelDeleteHandler(api));
        addHandler(new ChannelPinsUpdateHandler(api));
        addHandler(new ChannelUpdateHandler(api));
        addHandler(new WebhooksUpdateHandler(api));

        // user
        addHandler(new PresencesReplaceHandler(api));
        addHandler(new PresenceUpdateHandler(api));
        addHandler(new TypingStartHandler(api));
        addHandler(new UserUpdateHandler(api));

        // message
        addHandler(new MessageCreateHandler(api));
        addHandler(new MessageDeleteBulkHandler(api));
        addHandler(new MessageDeleteHandler(api));
        addHandler(new MessageUpdateHandler(api));

        // reaction
        addHandler(new MessageReactionAddHandler(api));
        addHandler(new MessageReactionRemoveAllHandler(api));
        addHandler(new MessageReactionRemoveHandler(api));
    }

    /**
     * Adds a packet handler.
     *
     * @param handler The handler to add.
     */
    private void addHandler(PacketHandler handler) {
        handlers.put(handler.getType(), handler);
    }

    /**
     * Gets the websocket of the adapter.
     *
     * @return The websocket of the adapter.
     */
    public WebSocket getWebSocket() {
        return websocket.get();
    }

    /**
     * Gets the Future which tells whether the connection is ready or failed.
     *
     * @return The Future.
     */
    public CompletableFuture<Boolean> isReady() {
        return ready;
    }

    /**
     * Sends the update status packet
     */
    public void updateStatus() {
        Optional<Activity> activity = api.getActivity();
        ObjectNode updateStatus = JsonNodeFactory.instance.objectNode()
                .put("op", GatewayOpcode.STATUS_UPDATE.getCode());
        ObjectNode data = updateStatus.putObject("d")
                .put("status", api.getStatus().getStatusString())
                .put("afk", false)
                .putNull("since");
        ObjectNode activityJson = data.putObject("game");
        activityJson.put("name", activity.isPresent() ? activity.get().getName() : null);
        activityJson.put("type", activity.map(g -> g.getType().getId()).orElse(0));
        activity.ifPresent(g -> g.getStreamingUrl().ifPresent(url -> activityJson.put("url", url)));
        logger.debug("Updating status (content: {})", updateStatus.toString());
        websocket.get().send(updateStatus.toString());
        this.lastFrameWasIdentify.set(false);
    }

    /**
     * Adds a server id to be queued for the "request guild members" packet.
     *
     * @param server The server.
     */
    public void queueRequestGuildMembers(Server server) {
        logger.debug("Queued {} for request guild members packet", server);
        requestGuildMembersQueue.add(server.getId());
    }

    @Override
    public void onFailure(WebSocket websocket, Throwable throwable, Response response) {
        if(logger.isTraceEnabled()) {
            logger.trace("onFailure: t='{}' response='{}'", throwable, response);
        }
        logger.warn("Websocket failure!", throwable);
    }
}