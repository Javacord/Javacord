package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import org.apache.logging.log4j.Logger;
import org.javacord.api.Javacord;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.logging.WebSocketLogger;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
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

    private final AtomicReference<WebSocket> websocket = new AtomicReference<>();

    /**
     * Created a new audio websocket adapter.
     *
     * @param connection The connection for the adapter.
     */
    public AudioWebSocketAdapter(AudioConnectionImpl connection) {
        this.connection = connection;
        connect();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        logger.debug("Received audio websocket packet: {}", text);
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
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
            logger.warn("An error occurred while connecting to audio websocket for audio connection", connection, t);
        }
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
        logger.debug("Sending voice identify packet for audio connection {}", connection);
        WebSocketFrame identifyFrame = WebSocketFrame.createTextFrame(identifyPacket.toString());
        websocket.sendFrame(identifyFrame);
    }
}
