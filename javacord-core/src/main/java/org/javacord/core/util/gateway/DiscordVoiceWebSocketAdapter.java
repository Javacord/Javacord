package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import org.apache.logging.log4j.Logger;
import org.javacord.api.Javacord;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.server.voice.VoiceServerUpdateListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.logging.WebSocketLogger;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The voice web socket adapter.
 */
//TODO: events
public class DiscordVoiceWebSocketAdapter extends WebSocketAdapter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordVoiceWebSocketAdapter.class);

    private final DiscordApiImpl api;
    private final Server server;
//    private final CompletableFuture<Boolean> ready = new CompletableFuture<>();

    private final AtomicReference<WebSocket> webSocket = new AtomicReference<>();
    private final AtomicReference<String> endpoint = new AtomicReference<>();
    private final AtomicReference<String> token = new AtomicReference<>();
    private final AtomicInteger ssrc = new AtomicInteger(1);
    private final AtomicReference<DatagramChannel> datagramChannel = new AtomicReference<>();

    private final AtomicReference<Future<?>> heartbeatTimer = new AtomicReference<>();
    private final AtomicReference<Integer> lastHeartbeatNonceSent = new AtomicReference<>();

//    private int lastSeq = -1;
//    private String sessionId = null;

//    private boolean reconnect = true;

//    private final AtomicMarkableReference<WebSocketFrame> lastSentFrameWasIdentify =
//            new AtomicMarkableReference<>(null, false);
//    private final AtomicReference<WebSocketFrame> nextHeartbeatFrame = new AtomicReference<>(null);
//    private final List<WebSocketListener> identifyFrameListeners = Collections.synchronizedList(new ArrayList<>());

//    // A reconnect attempt counter
//    private int reconnectAttempt = 0;

    /**
     * Creates a new discord voice web socket adapter.
     *
     * @param server The server to connect to.
     */
    public DiscordVoiceWebSocketAdapter(Server server) {
        this.server = server;
        api = ((DiscordApiImpl) server.getApi());
    }

    /**
     * Disconnects from the voice server and web socket.
     */
    public void disconnect() {
//        reconnect = false;
        sendVoiceStateUpdate(null, true, true);
        WebSocket webSocket = this.webSocket.get();
        if (webSocket != null) {
            webSocket.sendClose(WebSocketCloseReason.DISCONNECT.getNumericCloseCode());
        }
        // cancel heartbeat timer if within one minute no disconnect event was dispatched
        Future<?> oldFuture = heartbeatTimer.get();
        if (oldFuture != null) {
            api.getThreadPool().getDaemonScheduler().schedule(() -> heartbeatTimer.updateAndGet(future -> {
                if (future == oldFuture) {
                    future.cancel(false);
                    this.webSocket.set(null);
                    return null;
                }
                return future;
            }), 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Connects or moves to the given server voice channel.
     *
     * @param channel The channel to connect to.
     * @param muted Whether to connect self-muted.
     * @param deafened Whether to connect self-deafened.
     */
    public void connect(ServerVoiceChannel channel, boolean muted, boolean deafened) {
        if (!channel.getServer().equals(server)) {
            throw new IllegalArgumentException("Cannot connect to the voice channel of a foreign server");
        }
        if (webSocket.get() != null) {
            sendVoiceStateUpdate(channel, muted, deafened);
        } else {
            //TODO: async
            CountDownLatch voiceUpdateReplies = new CountDownLatch(2);

            if (api.getYourself().getConnectedVoiceChannels().contains(channel)) {
                // Discord thinks we are still in the channel, so just wait for the server update event
                voiceUpdateReplies.countDown();
            } else {
                AtomicReference<ListenerManager<ServerVoiceChannelMemberJoinListener>>
                        serverVoiceChannelMemberJoinListenerManager = new AtomicReference<>();
                serverVoiceChannelMemberJoinListenerManager.set(
                        channel.addServerVoiceChannelMemberJoinListener(event -> {
                            if (event.getUser().isYourself()) {
                                serverVoiceChannelMemberJoinListenerManager.get().remove();
                                voiceUpdateReplies.countDown();
                            }
                        }).removeAfter(10, TimeUnit.SECONDS));
            }

            AtomicReference<ListenerManager<VoiceServerUpdateListener>> voiceServerUpdateListenerManager =
                    new AtomicReference<>();
            voiceServerUpdateListenerManager.set(
                    server.addVoiceServerUpdateListener(event -> {
                        voiceServerUpdateListenerManager.get().remove();
                        token.set(event.getToken());
                        endpoint.set(event.getEndpoint().replace(":80", ""));
                        voiceUpdateReplies.countDown();
                    }).removeAfter(10, TimeUnit.SECONDS));

            sendVoiceStateUpdate(channel, muted, deafened);

            try {
                if (!voiceUpdateReplies.await(10, TimeUnit.SECONDS)) {
                    return;
                }
            } catch (InterruptedException ie) {
                return;
            }

            connect();
        }
    }

    /**
     * Connects the web socket.
     */
    private void connect() {
        WebSocketFactory factory = new WebSocketFactory();
        try {
            factory.setSSLContext(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("An error occurred while setting ssl context", e);
        }
        try {
            WebSocket webSocket = factory.createSocket(
                    "wss://" + endpoint.get() + "?v=" + Javacord.DISCORD_VOICE_GATEWAY_VERSION);
            this.webSocket.set(webSocket);
            webSocket.addHeader("Accept-Encoding", "gzip");
            webSocket.addListener(this);
            webSocket.addListener(new WebSocketLogger());
            webSocket.connect();
        } catch (Throwable t) {
            logger.warn("An error occurred while connecting to voice web socket", t);
//            if (reconnect) {
//                reconnectAttempt++;
//                logger.info("Trying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt));
//                // Reconnect after a (short?) delay depending on the amount of reconnect attempts
//                api.getThreadPool().getScheduler()
//                        .schedule(() -> {
//                            gatewayWriteLock.lock();
//                            try {
//                                gateway = null;
//                            } finally {
//                                gatewayWriteLock.unlock();
//                            }
//                            this.connect();
//                        }, api.getReconnectDelay(reconnectAttempt), TimeUnit.SECONDS);
//            }
        }
    }

    @Override
    public void onDisconnected(WebSocket webSocket, WebSocketFrame serverCloseFrame,
                               WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        //TODO:
//        if (closedByServer) {
//            String closeReason = serverCloseFrame != null ? serverCloseFrame.getCloseReason() : "unknown";
//            String closeCodeString;
//            if (serverCloseFrame != null) {
//                int code = serverCloseFrame.getCloseCode();
//                closeCodeString = WebSocketCloseCode.fromCode(code)
//                        .map(closeCode -> closeCode + " (" + code + ")")
//                        .orElseGet(() -> String.valueOf(code));
//            } else {
//                closeCodeString = "'unknown'";
//            }
//            logger.info("Websocket closed with reason '{}' and code {} by server!", closeReason, closeCodeString);
//        } else {
//            switch (clientCloseFrame == null ? -1 : clientCloseFrame.getCloseCode()) {
//                case com.neovisionaries.ws.client.WebSocketCloseCode.UNCONFORMED:
//                case com.neovisionaries.ws.client.WebSocketCloseCode.VIOLATED:
//                    logger.debug("Websocket closed!");
//                    break;
//                default:
//                    String closeReason = clientCloseFrame != null ? clientCloseFrame.getCloseReason() : "unknown";
//                    String closeCodeString;
//                    if (clientCloseFrame != null) {
//                        int code = clientCloseFrame.getCloseCode();
//                        closeCodeString = WebSocketCloseCode.fromCode(code)
//                                .map(closeCode -> closeCode + " (" + code + ")")
//                                .orElseGet(() -> String.valueOf(code));
//                    } else {
//                        closeCodeString = "'unknown'";
//                    }
//                    logger.info(
//                            "Websocket closed with reason '{}' and code {} by client!", closeReason, closeCodeString);
//                    break;
//            }
//        }
//
//        LostConnectionEvent lostConnectionEvent = new LostConnectionEvent(api);
//        List<LostConnectionListener> listeners = new ArrayList<>();
//        listeners.addAll(api.getLostConnectionListeners());
//        dispatchEvent(listeners, listener -> listener.onLostConnection(lostConnectionEvent));

        heartbeatTimer.updateAndGet(future -> {
            if (future != null) {
                future.cancel(false);
            }
            return null;
        });

//        if (!ready.isDone()) {
//            ready.complete(false);
//            return;
//        }
//
//        // Reconnect
//        if (reconnect) {
//            reconnectAttempt++;
//            logger.info("Trying to reconnect/resume in {} seconds!", api.getReconnectDelay(reconnectAttempt));
//            // Reconnect after a (short?) delay depending on the amount of reconnect attempts
//            api.getThreadPool().getScheduler()
//                    .schedule(this::connect, api.getReconnectDelay(reconnectAttempt), TimeUnit.SECONDS);
//        }

        this.webSocket.set(null);
    }

    @Override
    public void onConnected(WebSocket webSocket, Map<String, List<String>> headers) throws Exception {
        sendIdentify(webSocket);
        //TODO: resume
    }

    @Override
    public void onTextMessage(WebSocket webSocket, String text) throws Exception {
        ObjectMapper mapper = api.getObjectMapper();
        JsonNode packet = mapper.readTree(text);

        int op = packet.get("op").asInt();
        Optional<VoiceGatewayOpcode> opcode = VoiceGatewayOpcode.fromCode(op);
        if (!opcode.isPresent()) {
            logger.debug("Received unknown packet (op: {}, content: {})", op, packet.toString());
            return;
        }

        switch (opcode.get()) {
            case READY:
                logger.debug("Received READY packet");
//                reconnectAttempt = 0;
                JsonNode data = packet.get("d");
                ssrc.set(data.get("ssrc").asInt());
                int port = data.get("port").asInt();
                JsonNode modesNode = data.get("modes");
                List<String> modes = Stream.generate(modesNode.elements()::next)
                        .limit(modesNode.size())
                        .map(JsonNode::asText)
                        .collect(Collectors.toList());
                //TODO: async
                DatagramChannel datagramChannel = DatagramChannel.open();
                this.datagramChannel.set(datagramChannel);
                datagramChannel.connect(new InetSocketAddress(endpoint.get(), port));
                ByteBuffer ipDiscoveryPacket = ByteBuffer.allocate/*Direct*/(70);
                ipDiscoveryPacket.putInt(ssrc.get());
                ipDiscoveryPacket.rewind();
                datagramChannel.write(ipDiscoveryPacket);
                ipDiscoveryPacket.clear();
                datagramChannel.read(ipDiscoveryPacket);
                ipDiscoveryPacket.position(4);
                ipDiscoveryPacket.limit(70 - 2);
                StringBuilder localIp = new StringBuilder();
                while (ipDiscoveryPacket.hasRemaining()) {
                    char nextChar = (char) ipDiscoveryPacket.get();
                    if (nextChar == 0) {
                        break;
                    }
                    localIp.append(nextChar);
                }
                ipDiscoveryPacket.limit(70);
                ipDiscoveryPacket.order(ByteOrder.LITTLE_ENDIAN);
                int localPort = Short.toUnsignedInt(ipDiscoveryPacket.getShort(70 - 2));

                //TODO: select mode
                sendSelectProtocol(webSocket, localIp.toString(), localPort, "xsalsa20_poly1305");

                //TODO: remove
                api.getThreadPool().getExecutorService().submit(() -> {
                    try {
                        logger.error("udp reading started");
                        ipDiscoveryPacket.clear();
                        while (true) {
                            datagramChannel.receive(ipDiscoveryPacket);
                            logger.error("udp packet: {}", ipDiscoveryPacket.array());
                            ipDiscoveryPacket.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.error("udp reading stopped");
                });
//                api.getThreadPool().getExecutorService().submit(() -> {
//                    try {
//                        logger.error("udp reading started");
//                        InputStream cis = Channels.newInputStream(datagramChannel);
//                        int b;
//                        while ((b = cis.read()) != -1) {
//                            logger.error("udp byte: {}", b);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    logger.error("udp reading stopped");
//                });
                break;
            case SESSION_DESCRIPTION:
                logger.debug("Received SESSION_DESCRIPTION packet");
                JsonNode secretKeyNode = packet.get("d").get("secret_key");
                Iterator<JsonNode> secretKeyBytes = secretKeyNode.elements();
                ByteArrayOutputStream baos = new ByteArrayOutputStream(secretKeyNode.size());
                while (secretKeyBytes.hasNext()) {
                    baos.write(secretKeyBytes.next().intValue());
                }
                byte[] secretKey = baos.toByteArray();

                //TODO: remove
                sendSpeaking(webSocket, true);
//                this.datagramChannel.get().write(new SecretBox(ByteString.of(secretKey)).seal(ByteString.of(
//                        ((byte) 0x80), ((byte) 0x78), ((byte) 0), ((byte) 1), ((byte) 0), ((byte) 0), ((byte) 0),
//                        ((byte) 1), (byte) (ssrc.get()>>>3*4&0xFF), (byte) (ssrc.get()>>>2*4&0xFF),
//                        (byte) (ssrc.get()>>>1*4&0xFF), (byte) (ssrc.get()>>>0*4&0xFF), ((byte) 0), ((byte) 0),
//                        ((byte) 0), ((byte) 0), ((byte) 0), ((byte) 0), ((byte) 0), ((byte) 0), ((byte) 0),
//                        ((byte) 0), ((byte) 0), ((byte) 0)), ByteString.of(((byte) 0xF8), ((byte) 0xFF),
//                                                                           ((byte) 0xFE))).asByteBuffer());
//                this.datagramChannel.get().write(ByteString.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5,
//                                                               (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10)
//                                                         .asByteBuffer());
                break;
            case SPEAKING:
                logger.debug("Received SPEAKING packet");
                //TODO:
                //{"op":5,"d":{"user_id":"341505207341023233","ssrc":7,"speaking":true}}
                break;
            case HEARTBEAT_ACK:
                logger.debug("Received HEARTBEAT_ACK packet");
                lastHeartbeatNonceSent.updateAndGet(nonce -> nonce.equals(packet.get("d").asInt()) ? null : nonce);
                break;
            case HELLO:
                logger.debug("Received HELLO packet");

                //TODO: After bug in Discord mentioned at
                //      https://discordapp.com/developers/docs/topics/voice-connections#heartbeating got fixed,
                //      and changelog entry about it was published the multiplication should be removed
                int heartbeatInterval = Math.round(packet.get("d").get("heartbeat_interval").asInt() * 0.75F);
                heartbeatTimer.updateAndGet(future -> {
                    if (future != null) {
                        future.cancel(false);
                    }
                    return startHeartbeat(webSocket, heartbeatInterval);
                });
                break;
            case RESUMED:
                logger.debug("Received RESUMED packet");
                //TODO:
//                reconnectAttempt = 0;
//
//                ResumeEvent resumeEvent = new ResumeEvent(api);
//                List<ResumeListener> listeners = new ArrayList<>();
//                listeners.addAll(api.getResumeListeners());
//                dispatchEvent(listeners, listener -> listener.onResume(resumeEvent));
                break;
            case CLIENT_CONNECT:
                logger.debug("Received CLIENT_CONNECT packet");
                //{"op":12,"d":{"video_ssrc":0,"user_id":"341505207341023233","audio_ssrc":4}})
                break;
            case CLIENT_DISCONNECT:
                logger.debug("Received CLIENT_DISCONNECT packet");
                //{"op":13,"d":{"user_id":"341505207341023233"}}
                break;
            default:
                logger.debug("Received unknown packet (op: {}, content: {})", op, packet.toString());
                break;
        }
    }

    @Override
    public void onBinaryMessage(WebSocket webSocket, byte[] binary) throws Exception {
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
            onTextMessage(webSocket, message);
        } catch (UnsupportedEncodingException e) {
            logger.warn("An error occurred while decompressing data", e);
        }
    }

    /**
     * Starts the heartbeat.
     *
     * @param webSocket The web socket the heartbeat should be sent to.
     * @param heartbeatInterval The heartbeat interval.
     * @return The timer used for the heartbeat.
     */
    private Future<?> startHeartbeat(WebSocket webSocket, int heartbeatInterval) {
        // first heartbeat should assume last heartbeat was answered properly
        lastHeartbeatNonceSent.set(null);
        return api.getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
            try {
                if (lastHeartbeatNonceSent.get() == null) {
                    sendHeartbeat(webSocket);
                    logger.debug("Sent heartbeat (interval: {})", heartbeatInterval);
                } else {
                    webSocket.sendClose(WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getNumericCloseCode(),
                                        WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getCloseReason());
                }
            } catch (Throwable t) {
                logger.error("Failed to send heartbeat or close web socket!", t);
            }
        }, heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Sends the voice state update packet.
     *
     * @param channel The channel to send the voice state update for.
     */
    private void sendVoiceStateUpdate(ServerVoiceChannel channel, boolean muted, boolean deafened) {
        ObjectNode updateVoiceStatePacket = JsonNodeFactory.instance.objectNode()
                .put("op", GatewayOpcode.VOICE_STATE_UPDATE.getCode());
        updateVoiceStatePacket.putObject("d")
                .put("guild_id", server.getIdAsString())
                .put("channel_id", (channel == null) ? null : channel.getIdAsString())
                .put("self_mute", muted)
                .put("self_deaf", deafened);
        logger.debug("Sending VOICE_STATE_UPDATE packet for channel {} on server {}", channel, server);
        api.getWebSocketAdapter().getWebSocket().sendText(updateVoiceStatePacket.toString());
    }

    /**
     * Sends the select protocol packet.
     *
     * @param webSocket The web socket the select protocol packet should be sent to.
     * @param localIp The local ip to transmit.
     * @param localPort The local port to transmit.
     * @param mode The mode to transmit.
     */
    private void sendSelectProtocol(WebSocket webSocket, String localIp, int localPort, String mode) {
        ObjectNode selectProtocolPacket = JsonNodeFactory.instance.objectNode();
        selectProtocolPacket.put("op", VoiceGatewayOpcode.SELECT_PROTOCOL.getCode());
        ObjectNode data = selectProtocolPacket.putObject("d");
        data = data.put("protocol", "udp")
                .putObject("data");
        data.put("address", localIp)
                .put("port", localPort)
                .put("mode", mode);
        logger.debug("Sending SELECT_PROTOCOL packet");
        webSocket.sendText(selectProtocolPacket.toString());
    }

    /**
     * Sends the speaking packet.
     *
     * @param webSocket The web socket the select protocol packet should be sent to.
     */
    private void sendSpeaking(WebSocket webSocket, boolean speaking) {
        ObjectNode speakingPacket = JsonNodeFactory.instance.objectNode();
        speakingPacket.put("op", VoiceGatewayOpcode.SPEAKING.getCode());
        ObjectNode data = speakingPacket.putObject("d");
        data.put("user_id", "udp")
                .put("user_id", api.getYourself().getIdAsString())
                .put("ssrc", ssrc.get())
                .put("speaking", speaking);
        logger.debug("Sending SPEAKING packet");
        webSocket.sendText(speakingPacket.toString());
    }

    /**
     * Sends the heartbeat.
     *
     * @param webSocket The web socket the heartbeat should be sent to.
     */
    private void sendHeartbeat(WebSocket webSocket) {
        ObjectNode heartbeatPacket = JsonNodeFactory.instance.objectNode();
        heartbeatPacket.put("op", VoiceGatewayOpcode.HEARTBEAT.getCode());
        int nonce = (int) (Math.random() * Integer.MAX_VALUE);
        lastHeartbeatNonceSent.set(nonce);
        heartbeatPacket.put("d", nonce);
        webSocket.sendText(heartbeatPacket.toString());
    }

//    /**
//     * Sends the resume packet.
//     *
//     * @param websocket The websocket the resume packet should be sent to.
//     */
//    private void sendResume(WebSocket websocket) {
//        ObjectNode resumePacket = JsonNodeFactory.instance.objectNode()
//                .put("op", VoiceGatewayOpcode.RESUME.getCode());
//        resumePacket.putObject("d")
//                .put("token", api.getToken())
//                .put("session_id", sessionId)
//                .put("seq", lastSeq);
//        logger.debug("Sending resume packet");
//        websocket.sendText(resumePacket.toString());
//    }

    /**
     * Sends the identify packet.
     *
     * @param webSocket The web socket the identify packet should be sent to.
     */
    private void sendIdentify(WebSocket webSocket) {
        ObjectNode identifyPacket = JsonNodeFactory.instance.objectNode()
                .put("op", VoiceGatewayOpcode.IDENTIFY.getCode());
        ObjectNode data = identifyPacket.putObject("d");
        data.put("server_id", server.getIdAsString())
                .put("user_id", api.getYourself().getIdAsString())
                .put("session_id", api.getWebSocketAdapter().getSessionId())
                .put("token", token.get());
        logger.debug("Sending IDENTIFY packet");
        webSocket.sendText(identifyPacket.toString());
    }

//    /**
//     * Gets the websocket of the adapter.
//     *
//     * @return The websocket of the adapter.
//     */
//    public WebSocket getWebSocket() {
//        return websocket.get();
//    }
//
//    /**
//     * Gets the Future which tells whether the connection is ready or failed.
//     *
//     * @return The Future.
//     */
//    public CompletableFuture<Boolean> isReady() {
//        return ready;
//    }
//
//    /**
//     * Dispatches an event in a the listener thread.
//     *
//     * @param listeners The listeners for the event.
//     * @param consumer The consumer which consumes the listeners and calls the event.
//     * @param <T> The listener class.
//     */
//    protected <T> void dispatchEvent(List<T> listeners, Consumer<T> consumer) {
//        listenerExecutorService.submit(() -> listeners.stream().forEach(listener -> {
//            try {
//                consumer.accept(listener);
//            } catch (Throwable t) {
//                logger.error("An error occurred while calling a listener method!", t);
//            }
//        }));
//    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) {
        logger.warn("Voice web socket error!", cause);
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) {
        logger.error("Voice web socket callback error!", cause);
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
        logger.warn("Voice web socket onUnexpected error!", cause);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) {
        logger.warn("Voice web socket onConnect error!", exception);
    }

}
