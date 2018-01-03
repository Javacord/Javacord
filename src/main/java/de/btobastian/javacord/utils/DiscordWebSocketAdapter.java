package de.btobastian.javacord.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.*;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.events.connection.LostConnectionEvent;
import de.btobastian.javacord.events.connection.ReconnectEvent;
import de.btobastian.javacord.events.connection.ResumeEvent;
import de.btobastian.javacord.listeners.connection.LostConnectionListener;
import de.btobastian.javacord.listeners.connection.ReconnectListener;
import de.btobastian.javacord.listeners.connection.ResumeListener;
import de.btobastian.javacord.utils.handler.ReadyHandler;
import de.btobastian.javacord.utils.handler.ResumedHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelCreateHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelDeleteHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelUpdateHandler;
import de.btobastian.javacord.utils.handler.message.MessageCreateHandler;
import de.btobastian.javacord.utils.handler.message.MessageDeleteBulkHandler;
import de.btobastian.javacord.utils.handler.message.MessageDeleteHandler;
import de.btobastian.javacord.utils.handler.message.MessageUpdateHandler;
import de.btobastian.javacord.utils.handler.message.reaction.MessageReactionAddHandler;
import de.btobastian.javacord.utils.handler.message.reaction.MessageReactionRemoveAllHandler;
import de.btobastian.javacord.utils.handler.message.reaction.MessageReactionRemoveHandler;
import de.btobastian.javacord.utils.handler.server.*;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleCreateHandler;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleDeleteHandler;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleUpdateHandler;
import de.btobastian.javacord.utils.handler.user.PresenceUpdateHandler;
import de.btobastian.javacord.utils.handler.user.TypingStartHandler;
import de.btobastian.javacord.utils.handler.user.UserUpdateHandler;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
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

    private final DiscordApi api;
    private final HashMap<String, PacketHandler> handlers = new HashMap<>();
    private final CompletableFuture<Boolean> ready = new CompletableFuture<>();

    private WebSocket websocket = null;

    private Timer heartbeatTimer = null;

    private int heartbeatInterval = -1;
    private int lastSeq = -1;
    private String sessionId = null;

    private boolean reconnect = true;

    private final AtomicMarkableReference<WebSocketFrame> lastSentFrameWasIdentify =
            new AtomicMarkableReference<>(null, false);
    private final List<WebSocketListener> identifyFrameListeners = Collections.synchronizedList(new ArrayList<>());

    private long lastGuildMembersChunkReceived = System.currentTimeMillis();

    private final ExecutorService listenerExecutorService;

    // A reconnect attempt counter
    private int reconnectAttempt = 0;

    private static final Map<String, Long> lastIdentificationPerAccount = Collections.synchronizedMap(new HashMap<>());
    private static final ConcurrentMap<String, Semaphore> connectionDelaySemaphorePerAccount = new ConcurrentHashMap<>();

    static {
        // This scheduler makes sure that the semaphores get released after a while if it failed in the listener
        // for whatever reason. It's just a fail-safe.
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() ->
            connectionDelaySemaphorePerAccount.forEach((token, semaphore) -> {
                if ((semaphore.availablePermits() == 0) &&
                        ((System.currentTimeMillis() - lastIdentificationPerAccount.get(token)) >= 15000)) {
                    semaphore.release();
                }
            }), 10, 10, TimeUnit.SECONDS);
    }

    public DiscordWebSocketAdapter(DiscordApi api) {
        this.api = api;

        this.listenerExecutorService = api.getThreadPool().getSingleThreadExecutorService("listeners");

        registerHandlers();

        connect();
    }

    private static String getGateway(DiscordApi api) {
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
        websocket.sendClose(1000);
    }

    private void connect() {
        WebSocketFactory factory = new WebSocketFactory();
        try {
            factory.setSSLContext(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("An error occurred while setting ssl context", e);
        }
        try {
            websocket = factory.createSocket(getGateway(api) + "?encoding=json&v=" + Javacord.DISCORD_GATEWAY_PROTOCOL_VERSION);
            websocket.addHeader("Accept-Encoding", "gzip");
            websocket.addListener(this);
            String token = api.getToken();
            // identification is rate limited to once every 5 seconds, so don't try to more often per account, even in different instances
            connectionDelaySemaphorePerAccount.computeIfAbsent(token, key -> new Semaphore(1)).acquireUninterruptibly();
            for (long delay = 5100 - (System.currentTimeMillis() - lastIdentificationPerAccount.getOrDefault(token, 0L));
                 delay > 0;
                 delay = 5100 - (System.currentTimeMillis() - lastIdentificationPerAccount.getOrDefault(token, 0L))) {
                logger.debug("Delaying connecting by {}ms", delay);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ignored) { }
            }
            websocket.connect();
        } catch (IOException | WebSocketException e) {
            logger.warn("An error occurred while connecting to websocket", e);
            if (reconnect) {
                reconnectAttempt++;
                logger.info("Trying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt));
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
                        }, api.getReconnectDelay(reconnectAttempt), TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        if (sessionId == null) {
            sendIdentify(websocket);
        } else {
            sendResume(websocket);
        }
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                               WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        if (closedByServer) {
            logger.info("Websocket closed with reason {} and code {} by server!",
                    serverCloseFrame != null ? serverCloseFrame.getCloseReason() : "unknown",
                    serverCloseFrame != null ? serverCloseFrame.getCloseCode() : "unknown");
        } else {
            switch (clientCloseFrame == null ? -1 : clientCloseFrame.getCloseCode()) {
                case 1002:
                case 1008:
                    logger.debug("Websocket closed!");
                    break;
                default:
                    logger.info("Websocket closed with reason {} and code {} by client!",
                            clientCloseFrame != null ? clientCloseFrame.getCloseReason() : "unknown",
                            clientCloseFrame != null ? clientCloseFrame.getCloseCode() : "unknown");
                    break;
            }
        }

        LostConnectionEvent lostConnectionEvent = new LostConnectionEvent(api);
        List<LostConnectionListener> listeners = new ArrayList<>();
        listeners.addAll(api.getLostConnectionListeners());
        dispatchEvent(listeners, listener -> listener.onLostConnection(lostConnectionEvent));

        if (!ready.isDone()) {
            ready.complete(false);
            return;
        }

        // Reconnect
        if (heartbeatTimer != null) {
            heartbeatTimer.cancel();
            heartbeatTimer = null;
        }

        if (reconnect) {
            reconnectAttempt++;
            logger.info("Trying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt));
            // Reconnect after a (short?) delay depending on the amount of reconnect attempts
            api.getThreadPool().getScheduler()
                    .schedule(this::connect, api.getReconnectDelay(reconnectAttempt), TimeUnit.SECONDS);
        }
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        ObjectMapper mapper = api.getObjectMapper();
        JsonNode packet = mapper.readTree(text);

        int op = packet.get("op").asInt();

        switch (op) {
            case 0:
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
                    reconnectAttempt = 0;
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                    logger.debug("Received RESUMED packet");

                    ResumeEvent resumeEvent = new ResumeEvent(api);
                    List<ResumeListener> listeners = new ArrayList<>();
                    listeners.addAll(api.getResumeListeners());
                    dispatchEvent(listeners, listener -> listener.onResume(resumeEvent));
                }
                if (type.equals("READY")) {
                    reconnectAttempt = 0;
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                    sessionId = packet.get("d").get("session_id").asText();
                    // Discord sends us GUILD_CREATE packets after logging in. We will wait for them.
                    api.getThreadPool().getSingleThreadExecutorService("startupWait").submit(() -> {
                        boolean allUsersLoaded = false;
                        boolean allServersLoaded = false;
                        int lastUnavailableServerAmount = 0;
                        int sameUnavailableServerCounter = 0;
                        while (!allServersLoaded || !allUsersLoaded) {
                            if (api.getUnavailableServers().size() == lastUnavailableServerAmount) {
                                sameUnavailableServerCounter++;
                            } else {
                                lastUnavailableServerAmount = api.getUnavailableServers().size();
                                sameUnavailableServerCounter = 0;
                            }
                            allServersLoaded = api.getUnavailableServers().isEmpty();
                            if (allServersLoaded) {
                                allUsersLoaded = !api.getServers().stream()
                                        .filter(server -> server.getMemberCount() != server.getMembers().size())
                                        .findAny().isPresent();
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
                        ReconnectEvent reconnectEvent = new ReconnectEvent(api);
                        List<ReconnectListener> listeners = new ArrayList<>();
                        listeners.addAll(api.getReconnectListeners());
                        dispatchEvent(listeners, listener -> listener.onReconnect(reconnectEvent));
                        ready.complete(true);
                    });
                    logger.debug("Received READY packet");
                }
                break;
            case 1:
                sendHeartbeat(websocket);
                break;
            case 7:
                logger.debug("Received op 7 packet. Reconnecting...");
                websocket.sendClose(1000);
                connect();
                break;
            case 9:
                if (lastSentFrameWasIdentify.isMarked()) {
                    logger.info("Hit identifying rate limit. Retrying in 5 seconds...");
                    lastIdentificationPerAccount.put(api.getToken(), System.currentTimeMillis());
                    sendIdentify(websocket);
                } else {
                    // Invalid session :(
                    int zeroToFourSeconds = (int) (Math.random() * 4000);
                    logger.info("Could not resume session. Reconnecting in {}.{} seconds...",
                                1 + zeroToFourSeconds / 1000,
                                1 + zeroToFourSeconds / 100 % 10);
                    lastIdentificationPerAccount.put(api.getToken(), System.currentTimeMillis() - 4000 + zeroToFourSeconds);
                    sendIdentify(websocket);
                }
                break;
            case 10:
                JsonNode data = packet.get("d");
                heartbeatInterval = data.get("heartbeat_interval").asInt();
                logger.debug("Received HELLO packet");
                break;
            case 11:
                // heartbeat ack received
                break;
            default:
                logger.debug("Received unknown packet (op: {}, content: {})", op, packet.toString());
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
            onTextMessage(websocket, new String(decompressedData, "UTF-8"));
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
    private Timer startHeartbeat(final WebSocket websocket, final int heartbeatInterval) {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendHeartbeat(websocket);
                logger.debug("Sent heartbeat (interval: {})", heartbeatInterval);
            }
        }, 0, heartbeatInterval);
        return timer;
    }

    /**
     * Sends the heartbeat.
     *
     * @param websocket The websocket the heartbeat should be sent to.
     */
    private void sendHeartbeat(WebSocket websocket) {
        ObjectNode heartbeatPacket = JsonNodeFactory.instance.objectNode();
        heartbeatPacket.put("op", 1);
        heartbeatPacket.put("d", lastSeq);
        websocket.sendText(heartbeatPacket.toString());
    }

    /**
     * Sends the resume packet.
     *
     * @param websocket The websocket the resume packet should be sent to.
     */
    private void sendResume(WebSocket websocket) {
        ObjectNode resumePacket = JsonNodeFactory.instance.objectNode()
                .put("op", 6);
        resumePacket.putObject("d")
                .put("token", api.getToken())
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
                .put("op", 2);
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
        // remove eventually still registered listeners
        while (!identifyFrameListeners.isEmpty()) {
            websocket.removeListener(identifyFrameListeners.remove(0));
        }
        WebSocketFrame identifyFrame = WebSocketFrame.createTextFrame(identifyPacket.toString());
        lastSentFrameWasIdentify.set(identifyFrame, false);
        WebSocketAdapter identifyFrameListener = new WebSocketAdapter() {
            @Override
            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
                if (lastSentFrameWasIdentify.isMarked()) {
                    // sending frame after identify was sent => unset mark
                    lastSentFrameWasIdentify.set(null, false);
                    websocket.removeListener(this);
                    identifyFrameListeners.remove(this);
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
        addHandler(new ChannelUpdateHandler(api));

        // user
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
        return websocket;
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
     * Dispatches an event in a the listener thread.
     *
     * @param listeners The listeners for the event.
     * @param consumer The consumer which consumes the listeners and calls the event.
     * @param <T> The listener class.
     */
    protected <T> void dispatchEvent(List<T> listeners, Consumer<T> consumer) {
        listenerExecutorService.submit(() -> listeners.stream().forEach(listener -> {
            try {
                consumer.accept(listener);
            } catch (Throwable t) {
                logger.error("An error occurred while calling a listener method!", t);
            }
        }));
    }

    /**
     * Sends the update status packet
     */
    public void updateStatus() {
        Optional<Game> game = api.getGame();
        logger.debug("Updating status (game: {})", game.isPresent() ? game.get().getName() : "none");
        ObjectNode updateStatus = JsonNodeFactory.instance.objectNode()
                .put("op", 3);
        ObjectNode data = updateStatus.putObject("d")
                .put("status", "online")
                .put("afk", false)
                .putNull("since");
        ObjectNode gameJson = data.putObject("game");
        gameJson.put("name", game.isPresent() ? game.get().getName() : null);
        gameJson.put("type", game.map(g -> g.getType().getId()).orElse(0));
        game.ifPresent(g -> g.getStreamingUrl().ifPresent(url -> gameJson.put("url", url)));
        websocket.sendText(updateStatus.toString());
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        switch (cause.getMessage()) {
            case "Flushing frames to the server failed: Connection closed by remote host":
            case "Flushing frames to the server failed: Socket is closed":
            case "Flushing frames to the server failed: Connection has been shutdown: javax.net.ssl.SSLException:" +
                    " java.net.SocketException: Connection reset":
            case "An I/O error occurred while a frame was being read from the web socket: Connection reset":
                break;
            default:
                logger.warn("Websocket error!", cause);
                break;
        }
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        logger.error("Websocket callback error!", cause);
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        logger.warn("Websocket onUnexpected error!", cause);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        logger.warn("Websocket onConnect error!", exception);
    }
}