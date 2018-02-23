package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.ProxySettings;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import org.apache.logging.log4j.Logger;
import org.javacord.api.Javacord;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.connection.LostConnectionEvent;
import org.javacord.api.event.connection.ReconnectEvent;
import org.javacord.api.event.connection.ResumeEvent;
import org.javacord.api.util.auth.Authenticator;
import org.javacord.api.util.auth.Request;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.event.connection.LostConnectionEventImpl;
import org.javacord.core.event.connection.ReconnectEventImpl;
import org.javacord.core.event.connection.ResumeEventImpl;
import org.javacord.core.util.auth.NvWebSocketResponseImpl;
import org.javacord.core.util.auth.NvWebSocketRouteImpl;
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
import org.javacord.core.util.handler.guild.VoiceServerUpdateHandler;
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
import org.javacord.core.util.http.TrustAllTrustManager;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.logging.WebSocketLogger;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
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
import java.util.concurrent.atomic.AtomicMarkableReference;
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
public class DiscordWebSocketAdapter extends WebSocketAdapter {

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

    private volatile boolean reconnect;

    private final AtomicMarkableReference<WebSocketFrame> lastSentFrameWasIdentify =
            new AtomicMarkableReference<>(null, false);
    private final AtomicReference<WebSocketFrame> nextHeartbeatFrame = new AtomicReference<>(null);
    private final List<WebSocketListener> identifyFrameListeners = Collections.synchronizedList(new ArrayList<>());

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
                    if ((semaphore.availablePermits() == 0)
                            && (System.currentTimeMillis() - lastIdentificationPerAccount.getOrDefault(token, 0L)
                            >= 15000)) {
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
        this(api, true);
    }

    /**
     * Creates a new discord websocket adapter.
     *
     * @param api       The discord api instance.
     * @param reconnect Whether to try to reconnect.
     */
    DiscordWebSocketAdapter(DiscordApiImpl api, boolean reconnect) {
        this.api = api;
        this.reconnect = reconnect;

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
                                             requestGuildMembersPacket);
                                getWebSocket().sendText(requestGuildMembersPacket.toString());
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
     * Gets the current session id.
     *
     * @return The current session id.
     */
    protected String getSessionId() {
        return sessionId;
    }

    /**
     * Disconnects from the websocket.
     */
    public void disconnect() {
        reconnect = false;
        websocket.get().sendClose(WebSocketCloseReason.DISCONNECT.getNumericCloseCode());
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
            WebSocketFactory factory = new WebSocketFactory();
            String webSocketUri = getGateway(api) + "?encoding=json&v=" + Javacord.DISCORD_GATEWAY_VERSION;
            Proxy proxy = api.getProxy().orElseGet(() -> {
                List<Proxy> proxies = api.getProxySelector().orElseGet(ProxySelector::getDefault).select(URI.create(
                        webSocketUri.replace("wss://", "https://").replace("ws://", "http://")));

                return proxies.stream()
                        .filter(p -> p.type() == Proxy.Type.DIRECT)
                        .findAny()
                        .orElseGet(() -> proxies.stream()
                                .filter(p -> p.type() == Proxy.Type.HTTP)
                                .findAny()
                                .orElseGet(() -> proxies.get(0)));
            });
            switch (proxy.type()) {
                case DIRECT:
                    // nothing to do
                    break;

                case HTTP:
                    SocketAddress proxyAddress = proxy.address();
                    if (!(proxyAddress instanceof InetSocketAddress)) {
                        throw new WebSocketException(
                                null, "HTTP proxies without an InetSocketAddress are not supported currently");
                    }
                    InetSocketAddress proxyInetAddress = ((InetSocketAddress) proxyAddress);
                    String proxyHost = proxyInetAddress.getHostString();
                    int proxyPort = proxyInetAddress.getPort();
                    ProxySettings proxySettings = factory.getProxySettings();
                    proxySettings.setHost(proxyHost).setPort(proxyPort);

                    Optional<Authenticator> proxyAuthenticator = api.getProxyAuthenticator();
                    URL webSocketUrl = URI.create(
                            webSocketUri.replace("wss://", "https://").replace("ws://", "http://")).toURL();
                    if (proxyAuthenticator.isPresent()) {
                        Map<String, List<String>> requestHeaders = proxyAuthenticator.get().authenticate(
                                new NvWebSocketRouteImpl(webSocketUrl, proxy, proxyInetAddress),
                                new Request() { },
                                new NvWebSocketResponseImpl());
                        if (requestHeaders != null) {
                            requestHeaders.forEach((headerName, headerValues) -> {
                                if (headerValues == null) {
                                    proxySettings.getHeaders().remove(headerName);
                                    return;
                                }
                                if (headerValues.isEmpty()) {
                                    return;
                                }
                                String firstHeaderValue = headerValues.get(0);
                                if (firstHeaderValue == null) {
                                    proxySettings.getHeaders().remove(headerName);
                                } else {
                                    proxySettings.addHeader(headerName, firstHeaderValue);
                                }
                                headerValues.stream().skip(1).forEach(
                                        headerValue -> proxySettings.addHeader(headerName, headerValue));
                            });
                        }
                    } else {
                        PasswordAuthentication credentials = java.net.Authenticator.requestPasswordAuthentication(
                                proxyHost, proxyInetAddress.getAddress(), proxyPort, webSocketUrl.getProtocol(), null,
                                "Basic", webSocketUrl, java.net.Authenticator.RequestorType.PROXY);
                        if (credentials != null) {
                            proxySettings
                                    .setId(credentials.getUserName())
                                    .setPassword(String.valueOf(credentials.getPassword()));
                        }
                    }
                    break;

                default:
                    throw new WebSocketException(
                            null, "Proxies of type '" + proxy.type() + "' are not supported currently");
            }
            if (api.isTrustAllCertificates()) {
                factory.setSSLSocketFactory(new TrustAllTrustManager().createSslSocketFactory());
            }
            WebSocket websocket = factory.createSocket(webSocketUri);
            this.websocket.set(websocket);
            websocket.addHeader("Accept-Encoding", "gzip");
            websocket.addListener(this);
            websocket.addListener(new WebSocketLogger());
            waitForIdentifyRateLimit();
            websocket.connect();
        } catch (Throwable t) {
            logger.warn("An error occurred while connecting to websocket", t);
            if (reconnect) {
                reconnectAttempt.incrementAndGet();
                logger.info("Trying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt.get()));
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
        String token = api.getPrefixedToken();
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
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                               WebSocketFrame clientCloseFrame, boolean closedByServer) {
        Optional<WebSocketFrame> closeFrameOptional =
                Optional.ofNullable(closedByServer ? serverCloseFrame : clientCloseFrame);

        String closeReason = closeFrameOptional
                .map(WebSocketFrame::getCloseReason)
                .orElse("unknown");

        String closeCodeString = closeFrameOptional
                .map(closeFrame -> {
                    int code = closeFrame.getCloseCode();
                    return WebSocketCloseCode.fromCode(code)
                            .map(closeCode -> closeCode + " (" + code + ")")
                            .orElseGet(() -> String.valueOf(code));
                })
                .orElse("'unknown'");

        logger.info("Websocket closed with reason '{}' and code {} by {}!",
                    closeReason, closeCodeString, closedByServer ? "server" : "client");

        LostConnectionEvent lostConnectionEvent = new LostConnectionEventImpl(api);
        api.getEventDispatcher().dispatchLostConnectionEvent(null, lostConnectionEvent);

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

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        ObjectMapper mapper = api.getObjectMapper();
        JsonNode packet = mapper.readTree(text);

        int op = packet.get("op").asInt();
        Optional<GatewayOpcode> opcode = GatewayOpcode.fromCode(op);
        if (!opcode.isPresent()) {
            logger.debug("Received unknown packet (op: {}, content: {})", op, packet);
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
                    logger.debug("Received unknown packet of type {} (packet: {})", type, packet);
                }

                if (type.equals("GUILD_MEMBERS_CHUNK")) {
                    lastGuildMembersChunkReceived = System.currentTimeMillis();
                }
                if (type.equals("RESUMED")) {
                    reconnectAttempt.set(0);
                    logger.debug("Received RESUMED packet");

                    ResumeEvent resumeEvent = new ResumeEventImpl(api);
                    api.getEventDispatcher().dispatchResumeEvent(null, resumeEvent);
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
                            if (sameUnavailableServerCounter > 1000
                                    && lastGuildMembersChunkReceived + 5000 < System.currentTimeMillis()) {
                                // It has been more than 1 minute since no more servers became available and more
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
                        api.getEventDispatcher().dispatchReconnectEvent(null, reconnectEvent);
                        ready.complete(true);
                    });
                    logger.debug("Received READY packet");
                }
                break;
            case HEARTBEAT:
                sendHeartbeat(websocket);
                break;
            case RECONNECT:
                websocket.sendClose(WebSocketCloseReason.COMMANDED_RECONNECT.getNumericCloseCode(),
                                    WebSocketCloseReason.COMMANDED_RECONNECT.getCloseReason());
                break;
            case INVALID_SESSION:
                long fakeLastIdentificationTime = System.currentTimeMillis();
                if (lastSentFrameWasIdentify.isMarked()) {
                    logger.info("Hit identifying rate limit. Retrying in 5 seconds...");
                } else {
                    // Invalid session :(
                    int zeroToFourSeconds = (int) (Math.random() * 4000);
                    logger.info("Could not resume session. Reconnecting in {}.{} seconds...",
                            () -> 1 + zeroToFourSeconds / 1000,
                            () -> 1 + zeroToFourSeconds / 100 % 10);
                    fakeLastIdentificationTime -= 4000 - zeroToFourSeconds;
                }
                lastIdentificationPerAccount.put(api.getPrefixedToken(), fakeLastIdentificationTime);
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
                    connectionDelaySemaphorePerAccount.get(api.getPrefixedToken()).release();
                    sendResume(websocket);
                }
                break;
            case HEARTBEAT_ACK:
                logger.debug("Heartbeat ACK received");
                heartbeatAckReceived.set(true);
                break;
            default:
                logger.debug("Received unknown packet (op: {}, content: {})", op, packet);
                break;
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        Inflater decompressor = new Inflater();
        decompressor.setInput(binary);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(binary.length);
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
            onTextMessage(websocket, message);
        } catch (UnsupportedEncodingException e) {
            logger.warn("An error occurred while decompressing data", e);
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
                    websocket.sendClose(WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getNumericCloseCode(),
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
        WebSocketFrame heartbeatFrame = WebSocketFrame.createTextFrame(heartbeatPacket.toString());
        nextHeartbeatFrame.set(heartbeatFrame);
        websocket.sendFrame(heartbeatFrame);
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
                .put("token", api.getPrefixedToken())
                .put("session_id", sessionId)
                .put("seq", lastSeq);
        logger.debug("Sending resume packet");
        websocket.sendText(resumePacket.toString());
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
        String token = api.getPrefixedToken();
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
        // remove eventually still registered listeners
        synchronized (identifyFrameListeners) {
            websocket.removeListeners(identifyFrameListeners);
            identifyFrameListeners.clear();
        }
        WebSocketFrame identifyFrame = WebSocketFrame.createTextFrame(identifyPacket.toString());
        lastSentFrameWasIdentify.set(identifyFrame, false);
        WebSocketAdapter identifyFrameListener = new WebSocketAdapter() {
            @Override
            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
                if (lastSentFrameWasIdentify.isMarked()) {
                    // sending non-heartbeat frame after identify was sent => unset mark
                    if (!nextHeartbeatFrame.compareAndSet(frame, null)) {
                        lastSentFrameWasIdentify.set(null, false);
                        websocket.removeListener(this);
                        identifyFrameListeners.remove(this);
                    }
                } else {
                    // identify frame is actually sent => set the mark
                    if (lastSentFrameWasIdentify.compareAndSet(frame, null, false, true)) {
                        lastIdentificationPerAccount.put(token, System.currentTimeMillis());
                        connectionDelaySemaphorePerAccount.get(token).release();
                    }
                }
            }
        };
        identifyFrameListeners.add(identifyFrameListener);
        websocket.addListener(identifyFrameListener);
        logger.debug("Sending identify packet");
        websocket.sendFrame(identifyFrame);
    }

    /**
     * Sends the voice state update packet.
     *
     * @param server The server to send the voice state update for. Can be {@code null} if {@code channel} is given.
     * @param channel The channel to connect to or {@code null} to disconnect from voice.
     * @param selfMuted Whether to self-mute on the given server. If {@code null}, current state remains unchanged.
     * @param selfDeafened Whether to self-deafen on the given server. If {@code null}, current state remains unchanged.
     */
    public void sendVoiceStateUpdate(
            Server server, ServerVoiceChannel channel, Boolean selfMuted, Boolean selfDeafened) {
        ObjectNode updateVoiceStatePacket = JsonNodeFactory.instance.objectNode()
                .put("op", GatewayOpcode.VOICE_STATE_UPDATE.getCode());
        if (server == null) {
            if (channel == null) {
                throw new IllegalArgumentException("Either server or channel must be given");
            }
            server = channel.getServer();
        }
        User yourself = api.getYourself();
        updateVoiceStatePacket.putObject("d")
                .put("guild_id", server.getIdAsString())
                .put("channel_id", (channel == null) ? null : channel.getIdAsString())
                .put("self_mute", (selfMuted == null) ? server.isSelfMuted(yourself) : selfMuted)
                .put("self_deaf", (selfDeafened == null) ? server.isSelfDeafened(yourself) : selfDeafened);
        logger.debug("Sending VOICE_STATE_UPDATE packet for channel {} on server {}", channel, server);
        websocket.get().sendText(updateVoiceStatePacket.toString());
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
        addHandler(new VoiceServerUpdateHandler(api));

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
     * Sends the update status packet.
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
        logger.debug("Updating status (content: {})", updateStatus);
        websocket.get().sendText(updateStatus.toString());
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
    public void onError(WebSocket websocket, WebSocketException cause) {
        switch (cause.getMessage()) {
            // TODO This is copied from v2. I'm unsure if that's something we should do. Probably not ^^
            case "Flushing frames to the server failed: Connection closed by remote host":
            case "Flushing frames to the server failed: Socket is closed":
            case "Flushing frames to the server failed: Connection has been shutdown: javax.net.ssl.SSLException:"
                    + " java.net.SocketException: Connection reset":
            case "An I/O error occurred while a frame was being read from the web socket: Connection reset":
                break;
            default:
                logger.warn("Websocket error!", cause);
                break;
        }
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) {
        logger.error("Websocket callback error!", cause);
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
        logger.warn("Websocket onUnexpected error!", cause);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) {
        logger.warn("Websocket onConnect error!", exception);
    }
}
