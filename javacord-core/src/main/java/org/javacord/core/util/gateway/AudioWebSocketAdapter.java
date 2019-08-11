package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import org.apache.logging.log4j.Logger;
import org.javacord.api.Javacord;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.logging.WebSocketLogger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.DataFormatException;

public class AudioWebSocketAdapter extends WebSocketAdapter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AudioWebSocketAdapter.class);

    /**
     * The audio connection for this websocket.
     */
    private final AudioConnectionImpl connection;

    private final DiscordApiImpl api;

    private final AtomicReference<WebSocket> websocket = new AtomicReference<>();

    private final Heart heart;

    private AudioUdpSocket socket;
    private int ssrc;

    /**
     * A boolean to indicate if the websocket should try to reconnect.
     *
     * <p>Used to prevent automatic reconnects after a wanted websocket close.
     */
    private volatile boolean reconnect;

    /**
     * Whether a {@link VoiceGatewayOpcode#RESUME} should be sent or a normal connect.
     */
    private volatile boolean sendResume;

    // A reconnect attempt counter
    private final AtomicInteger reconnectAttempt = new AtomicInteger();

    /**
     * Created a new audio websocket adapter.
     *
     * @param connection The connection for the adapter.
     */
    public AudioWebSocketAdapter(AudioConnectionImpl connection) {
        this.connection = connection;
        reconnect = true;
        api = (DiscordApiImpl) connection.getChannel().getApi();
        heart = new Heart(
                api,
                heartbeatFrame -> websocket.get().sendFrame(heartbeatFrame),
                (code, reason) -> websocket.get().sendClose(code, reason),
                true);
        connect();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        ObjectMapper mapper = api.getObjectMapper();
        JsonNode packet = mapper.readTree(text);

        heart.handlePacket(packet);

        int op = packet.get("op").asInt();
        Optional<VoiceGatewayOpcode> opcode = VoiceGatewayOpcode.fromCode(op);
        if (!opcode.isPresent()) {
            logger.debug("Received unknown audio websocket packet ({}, op: {}, content: {})",
                    connection, op, packet);
            return;
        }

        switch (opcode.get()) {
            case HELLO:
                logger.debug("Received {} packet for {}", opcode.get().name(), connection);
                if (!sendResume) {
                    sendIdentify(websocket);
                }
                JsonNode data = packet.get("d");
                int heartbeatInterval = data.get("heartbeat_interval").asInt();
                heart.startBeating(heartbeatInterval);
                break;
            case READY:
                logger.debug("Received {} packet for {}", opcode.get().name(), connection);
                data = packet.get("d");

                String ip = data.get("ip").asText();
                int port = data.get("port").asInt();
                ssrc = data.get("ssrc").asInt();

                socket = new AudioUdpSocket(connection, new InetSocketAddress(ip, port), ssrc);
                sendSelectProtocol(websocket);

                // TODO remove
                sendSpeaking(websocket, false);
                Thread.sleep(1000);
                break;
            case SESSION_DESCRIPTION:
                data = packet.get("d");
                byte[] secretKey = api.getObjectMapper().convertValue(data.get("secret_key"), byte[].class);
                socket.setSecretKey(secretKey);
                socket.startSending();
                // We established a connection with the udp socket. Now we are ready to send audio! :-)
                connection.getReadyFuture().complete(connection);
                break;
            case HEARTBEAT_ACK:
                // Handled in the heart
                break;
            case RESUMED:
                logger.info("Successfully resumed audio websocket connection for {}", connection);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        String message;
        try {
            message = BinaryMessageDecompressor.decompress(binary);
        } catch (DataFormatException e) {
            logger.warn("An error occurred while decompressing data", e);
            return;
        }
        logger.trace("onTextMessage: text='{}'", message);
        onTextMessage(websocket, message);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        if (sendResume) {
            sendResume(websocket);
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

        if (!reconnect) {
            // Remove the connection from the server to allow the user to establish a new connection
            ((ServerImpl) connection.getServer()).removeAudioConnection(connection);
        }

        logger.info("Websocket closed with reason '{}' and code {} by {} for {}!",
                closeReason, closeCodeString, closedByServer ? "server" : "client", connection);

        // Squash it, until it stops beating
        heart.squash();

        if (connection.getDisconnectFuture() != null) {
            connection.getDisconnectFuture().complete(null);
        }

        if (!connection.getReadyFuture().isDone()) {
            connection.getReadyFuture().completeExceptionally(
                    new IllegalStateException(
                            "Audio websocket closed with reason '"
                            + closeReason
                            + "' and code "
                            + closeCodeString
                            + " by "
                            + (closedByServer ? "server" : "client")
                            + " before "
                            + VoiceGatewayOpcode.SESSION_DESCRIPTION.name()
                            + " packet was received"
                    )
            );
        }

        if (reconnect) {
            sendResume = !closeFrameOptional
                    .map(WebSocketFrame::getCloseCode)
                    .flatMap(WebSocketCloseCode::fromCode)
                    .map(WebSocketCloseCode.SESSION_TIMEOUT::equals)// Do not send resume
                    .orElse(false);
            reconnectAttempt.incrementAndGet();
            logger.info("Trying to reconnect/resume audio websocket in {} seconds!",
                    api.getReconnectDelay(reconnectAttempt.get()));
            // Reconnect after a (short?) delay depending on the amount of reconnect attempts
            api.getThreadPool().getScheduler()
                    .schedule(this::connect, api.getReconnectDelay(reconnectAttempt.get()), TimeUnit.SECONDS);
        }
    }

    /**
     * Connects the websocket.
     */
    private void connect() {
        String endpoint = "wss://"
                + connection.getEndpoint().replace(":80", "")
                + "?v="
                + Javacord.DISCORD_VOICE_GATEWAY_VERSION;
        logger.debug("Trying to connect to websocket {}", endpoint);
        WebSocketFactory factory = new WebSocketFactory();
        try {
            factory.setSSLContext(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("An error occurred while setting ssl context", e);
        }
        try {
            WebSocket websocket = factory
                    .createSocket(endpoint);
            this.websocket.set(websocket);
            websocket.addHeader("Accept-Encoding", "gzip");
            websocket.addListener(this);
            websocket.addListener(new WebSocketLogger());
            websocket.connect();
        } catch (Throwable t) {
            logger.warn("An error occurred while connecting to audio websocket for {}", connection, t);
            if (reconnect) {
                sendResume = false;
                reconnectAttempt.incrementAndGet();
                logger.info("Trying to reconnect/resume audio websocket in {} seconds!",
                        api.getReconnectDelay(reconnectAttempt.get()));
                // Reconnect after a (short?) delay depending on the amount of reconnect attempts
                api.getThreadPool().getScheduler()
                        .schedule(this::connect, api.getReconnectDelay(reconnectAttempt.get()), TimeUnit.SECONDS);
            }
        }
    }

    /**
     * Disconnects from the websocket.
     */
    public void disconnect() {
        reconnect = false;
        socket.stopSending();
        websocket.get().sendClose(WebSocketCloseReason.DISCONNECT.getNumericCloseCode());
        // cancel heartbeat timer if within one minute no disconnect event was dispatched
        api.getThreadPool().getDaemonScheduler().schedule(heart::squash, 1, TimeUnit.MINUTES);
    }

    /**
     * Sends the resume packet.
     *
     * @param websocket The websocket the resume packet should be sent to.
     */
    private void sendResume(WebSocket websocket) {
        ObjectNode resumePacket = JsonNodeFactory.instance.objectNode()
                .put("op", VoiceGatewayOpcode.RESUME.getCode());
        ObjectNode data = resumePacket.putObject("d");
        data.put("server_id", connection.getServer().getIdAsString())
                .put("session_id", connection.getSessionId())
                .put("token", connection.getToken());
        logger.debug("Sending resume packet for {}", connection);
        WebSocketFrame resumeFrame = WebSocketFrame.createTextFrame(resumePacket.toString());
        websocket.sendFrame(resumeFrame);
    }

    /**
     * Sends the identify packet.
     *
     * @param websocket The websocket the identify packet should be sent to.
     */
    private void sendIdentify(WebSocket websocket) {
        ObjectNode identifyPacket = JsonNodeFactory.instance.objectNode()
                .put("op", VoiceGatewayOpcode.IDENTIFY.getCode());
        ObjectNode data = identifyPacket.putObject("d");
        data.put("server_id", connection.getServer().getIdAsString())
                .put("user_id", connection.getServer().getApi().getYourself().getIdAsString())
                .put("session_id", connection.getSessionId())
                .put("token", connection.getToken());
        logger.debug("Sending voice identify packet for {}", connection);
        WebSocketFrame identifyFrame = WebSocketFrame.createTextFrame(identifyPacket.toString());
        websocket.sendFrame(identifyFrame);
    }

    /**
     * Sends a "select protocol" packet.
     *
     * @param websocket The websocket the packet should be sent to.
     * @throws IOException  If an I/O error occurs.
     */
    private void sendSelectProtocol(WebSocket websocket) throws IOException {
        InetSocketAddress address = socket.discoverIp();
        ObjectNode selectProtocolPacket = JsonNodeFactory.instance.objectNode();
        selectProtocolPacket
                .put("op", VoiceGatewayOpcode.SELECT_PROTOCOL.getCode())
                .putObject("d")
                .put("protocol", "udp")
                .putObject("data")
                .put("address", address.getHostString())
                .put("port", address.getPort())
                .put("mode", "xsalsa20_poly1305");
        logger.debug("Sending select protocol packet for {}", connection);
        WebSocketFrame selectProtocolFrame = WebSocketFrame.createTextFrame(selectProtocolPacket.toString());
        websocket.sendFrame(selectProtocolFrame);
    }

    /**
     * Sends the speaking packet.
     *
     * @param websocket The websocket the packet should be sent to.
     * @param speaking Whether speaking should be displayed or not.
     */
    private void sendSpeaking(WebSocket websocket, boolean speaking) {
        ObjectNode speakingPacket = JsonNodeFactory.instance.objectNode();
        speakingPacket
                .put("op", VoiceGatewayOpcode.SPEAKING.getCode())
                .putObject("d")
                .put("speaking", speaking ? 1 : 0)
                .put("delay", 0)
                .put("ssrc", ssrc);
        logger.debug("Sending speaking packet for {} (packet: {})", connection, speakingPacket);
        WebSocketFrame speakingFrame = WebSocketFrame.createTextFrame(speakingPacket.toString());
        websocket.sendFrame(speakingFrame);
    }

    /**
     * Sends the speaking packet.
     *
     * @param speaking Whether speaking should be displayed or not.
     */
    public void sendSpeaking(boolean speaking) {
        sendSpeaking(websocket.get(), speaking);
    }
}
