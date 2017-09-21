package de.btobastian.javacord.utils;

import com.neovisionaries.ws.client.*;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.utils.handler.ReadyHandler;
import de.btobastian.javacord.utils.handler.ResumedHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelCreateHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelDeleteHandler;
import de.btobastian.javacord.utils.handler.message.MessageCreateHandler;
import de.btobastian.javacord.utils.handler.message.MessageDeleteHandler;
import de.btobastian.javacord.utils.handler.message.MessageUpdateHandler;
import de.btobastian.javacord.utils.handler.message.reaction.MessageReactionAddHandler;
import de.btobastian.javacord.utils.handler.message.reaction.MessageReactionRemoveHandler;
import de.btobastian.javacord.utils.handler.server.GuildCreateHandler;
import de.btobastian.javacord.utils.handler.server.GuildDeleteHandler;
import de.btobastian.javacord.utils.handler.server.GuildMembersChunkHandler;
import de.btobastian.javacord.utils.handler.user.PresenceUpdateHandler;
import de.btobastian.javacord.utils.handler.user.TypingStartHandler;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The main websocket adapter.
 */
public class DiscordWebsocketAdapter extends WebSocketAdapter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordWebsocketAdapter.class);

    private final DiscordApi api;
    private final HashMap<String, PacketHandler> handlers = new HashMap<>();
    private final CompletableFuture<Boolean> ready = new CompletableFuture<>();
    private final String gateway;

    private WebSocket websocket = null;

    private Timer heartbeatTimer = null;

    private int heartbeatInterval = -1;
    private int lastSeq = -1;
    private String sessionId = null;

    private boolean heartbeatAckReceived = false;

    private boolean reconnect = true;

    private long lastGuildMembersChunkReceived = System.currentTimeMillis();

    // We allow 5 reconnects per 5 minutes.
    // This limit should never be hit under normal conditions, but prevent reconnect loops.
    private Queue<Long> ratelimitQueue = new LinkedList<>();
    private int reconnectAttempts = 5;
    private int ratelimitResetIntervalInSeconds = 5*60;

    public DiscordWebsocketAdapter(DiscordApi api, String gateway) {
        this.api = api;
        this.gateway = gateway;

        registerHandlers();

        connect();
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
            websocket = factory.createSocket(gateway + "?encoding=json&v=" + Javacord.DISCORD_GATEWAY_PROTOCOL_VERSION);
            websocket.addHeader("Accept-Encoding", "gzip");
            websocket.addListener(this);
            websocket.connect();
        } catch (IOException | WebSocketException e) {
            logger.warn("An error occurred while connecting to websocket", e);
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
                    logger.debug("Websocket closed! Trying to resume connection.");
                    break;
                default:
                    logger.info("Websocket closed with reason {} and code {} by client!",
                            clientCloseFrame != null ? clientCloseFrame.getCloseReason() : "unknown",
                            clientCloseFrame != null ? clientCloseFrame.getCloseCode() : "unknown");
                    break;
            }
        }

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
            ratelimitQueue.offer(System.currentTimeMillis());
            if (ratelimitQueue.size() > reconnectAttempts) {
                long timestamp = ratelimitQueue.poll();
                if (System.currentTimeMillis() - (1000*ratelimitResetIntervalInSeconds) < timestamp) {
                    logger.error("Websocket connection failed more than {} times in the last {} seconds!" +
                            " Stopping reconnecting.", reconnectAttempts, ratelimitResetIntervalInSeconds);
                    return;
                }
            }
            connect();
        }
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        JSONObject packet = new JSONObject(text);

        int op = packet.getInt("op");

        switch (op) {
            case 0:
                lastSeq = packet.getInt("s");
                String type = packet.getString("t");
                PacketHandler handler = handlers.get(type);
                if (handler != null) {
                    handler.handlePacket(packet.getJSONObject("d"));
                } else {
                    logger.debug("Received unknown packet of type {} (packet: {})", type, packet.toString());
                }

                if (type.equals("GUILD_MEMBERS_CHUNK")) {
                    lastGuildMembersChunkReceived = System.currentTimeMillis();
                }
                if (type.equals("RESUMED")) {
                    // We are the one who send the first heartbeat
                    heartbeatAckReceived = true;
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                    logger.debug("Received RESUMED packet");
                }
                if (type.equals("READY") && sessionId == null) {
                    // We are the one who send the first heartbeat
                    heartbeatAckReceived = true;
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                    sessionId = packet.getJSONObject("d").getString("session_id");
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
                        ready.complete(true);
                    });
                    logger.debug("Received READY packet");
                } else if (type.equals("READY")) {
                    heartbeatAckReceived = true;
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
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
                // Invalid session :(
                logger.info("Could not resume session. Reconnecting in 5 seconds...");
                Thread.sleep(5000);
                sendIdentify(websocket);
                break;
            case 10:
                JSONObject data = packet.getJSONObject("d");
                heartbeatInterval = data.getInt("heartbeat_interval");
                logger.debug("Received HELLO packet");
                break;
            case 11:
                heartbeatAckReceived = true;
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
                heartbeatAckReceived = false;
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
        JSONObject heartbeatPacket = new JSONObject();
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
        JSONObject resumePacket = new JSONObject()
                .put("op", 6)
                .put("d", new JSONObject()
                        .put("token", api.getToken())
                        .put("session_id", sessionId)
                        .put("seq", lastSeq));
        logger.debug("Sending resume packet");
        websocket.sendText(resumePacket.toString());
    }

    /**
     * Sends the identify packet.
     *
     * @param websocket The websocket the identify packet should be sent to.
     */
    private void sendIdentify(WebSocket websocket) {
        JSONObject identifyPacket = new JSONObject()
                .put("op", 2)
                .put("d", new JSONObject()
                        .put("token", api.getToken())
                        .put("properties", new JSONObject()
                                .put("$os", System.getProperty("os.name"))
                                .put("$browser", "Javacord")
                                .put("$device", "Javacord")
                                .put("$referrer", "")
                                .put("$referring_domain", ""))
                        .put("compress", true)
                        .put("large_threshold", 250));
        if (api.getTotalShards() > 1) {
            identifyPacket.getJSONObject("d").put("shard", new int[]{api.getCurrentShard(), api.getTotalShards()});
        }
        logger.debug("Sending identify packet");
        websocket.sendText(identifyPacket.toString());
    }

    /**
     * Registers all handlers.
     */
    private void registerHandlers() {
        // general
        addHandler(new ReadyHandler(api));
        addHandler(new ResumedHandler(api));

        // servers
        addHandler(new GuildCreateHandler(api));
        addHandler(new GuildDeleteHandler(api));
        addHandler(new GuildMembersChunkHandler(api));

        // channel
        addHandler(new ChannelCreateHandler(api));
        addHandler(new ChannelDeleteHandler(api));

        // user
        addHandler(new PresenceUpdateHandler(api));
        addHandler(new TypingStartHandler(api));

        // message
        addHandler(new MessageCreateHandler(api));
        addHandler(new MessageDeleteHandler(api));
        addHandler(new MessageUpdateHandler(api));

        // reaction
        addHandler(new MessageReactionAddHandler(api));
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
     * Sends the update status packet
     */
    public void updateStatus() {
        Optional<Game> game = api.getGame();
        logger.debug("Updating status (game: {})", game.isPresent() ? game.get().getName() : "none");
        JSONObject gameJson = new JSONObject();
        gameJson.put("name", game.isPresent() ? game.get().getName() : JSONObject.NULL);
        gameJson.put("type", game.isPresent() ? game.get().getType().getId() : 0);
        game.ifPresent(g -> g.getStreamingUrl().ifPresent(url -> gameJson.put("url", url)));
        JSONObject updateStatus = new JSONObject()
                .put("op", 3)
                .put("d", new JSONObject()
                        .put("status", "online")
                        .put("afk", false)
                        .put("game", gameJson)
                        .put("since", JSONObject.NULL));
        websocket.sendText(updateStatus.toString());
    }

    /**
     * Sets the reconnect reset interval in seconds.
     *
     * @param ratelimitResetIntervalInSeconds The reconnect reset interval in seconds.
     */
    public void setRatelimitResetIntervalInSeconds(int ratelimitResetIntervalInSeconds) {
        this.ratelimitResetIntervalInSeconds = ratelimitResetIntervalInSeconds;
    }

    /**
     * Sets the maximum reconnect attempts.
     *
     * @param reconnectAttempts The maximum reconnect attempts.
     */
    public void setReconnectAttempts(int reconnectAttempts) {
        this.reconnectAttempts = reconnectAttempts;
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
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        logger.warn("Websocket onUnexpected error!", cause);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        logger.warn("Websocket onConnect error!", exception);
    }
}