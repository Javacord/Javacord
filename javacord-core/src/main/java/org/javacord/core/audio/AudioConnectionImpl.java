package org.javacord.core.audio;

import org.apache.logging.log4j.Logger;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.core.util.logging.LoggerUtil;

public class AudioConnectionImpl implements AudioConnection {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AudioConnectionImpl.class);

    /**
     * The voice channel of the audio connection.
     */
    private final ServerVoiceChannel channel;

    /**
     * Whether the audio connection is currently connecting or already connected.
     */
    private volatile boolean connectingOrConnected = false;

    /**
     * The session id of the audio connection.
     */
    private volatile String sessionId;

    /**
     * The token for the audio connection.
     */
    private volatile String token;

    /**
     * The endpoint for the audio websocket.
     */
    private volatile String endpoint;

    /**
     * Creates a new audi connection.
     *
     * @param channel The channel of the audio connection.
     */
    public AudioConnectionImpl(ServerVoiceChannel channel) {
        this.channel = channel;
    }

    /**
     * Sets the session id of the connection.
     *
     * @param sessionId The session id of the connection.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Sets the token for the audio connection.
     *
     * @param token The token for the audio connection.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Sets the endpoint for the audio websocket.
     *
     * @param endpoint The endpoint for the audio websocket.
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Tries to establish a connection if all required information is available and there's not already a connection.
     *
     * @return Whether it will try to connect or not.
     */
    public synchronized boolean tryConnect() {
        if (connectingOrConnected || this.sessionId == null || this.token == null || this.endpoint == null) {
            return false;
        }
        connectingOrConnected = true;
        logger.debug("Received all information required to connect to voice channel {}", channel);
        return true;
    }

    @Override
    public ServerVoiceChannel getChannel() {
        return channel;
    }
}
