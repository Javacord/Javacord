package org.javacord.core.audio;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.SpeakingFlag;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.audio.InternalAudioConnectionAttachableListenerManager;
import org.javacord.core.util.concurrent.BlockingReference;
import org.javacord.core.util.gateway.AudioWebSocketAdapter;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioConnectionImpl implements AudioConnection, InternalAudioConnectionAttachableListenerManager {

    /**
     * An internal counter for the audio connection id.
     */
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AudioConnectionImpl.class);

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The voice channel of the audio connection.
     */
    private volatile ServerVoiceChannel channel;

    /**
     * A future that finishes once the connection is fully established.
     */
    private final CompletableFuture<AudioConnection> readyFuture;

    /**
     * A future that finishes once the connection has been moved to a different channel.
     */
    private CompletableFuture<Void> movingFuture;

    /**
     * A future that finishes once the connection has been disconnected.
     */
    private CompletableFuture<Void> disconnectFuture;

    /**
     * The source that is currently being played.
     */
    private final BlockingReference<AudioSource> currentSource = new BlockingReference<>();

    /**
     * An artificial id for the connection.
     */
    private final long id;

    /**
     * The websocket adapter for this audio connection.
     */
    private volatile AudioWebSocketAdapter websocketAdapter;

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
     * The current set of active speaking flags.
     */
    private volatile EnumSet<SpeakingFlag> speakingFlags = EnumSet.noneOf(SpeakingFlag.class);

    /**
     * Whether the bot is muted or not.
     */
    private volatile boolean muted;

    /**
     * Whether the bot is deafened or not.
     */
    private volatile boolean deafened;

    /**
     * Creates a new audi connection.
     *
     * @param channel The channel of the audio connection.
     * @param readyFuture An uncompleted future that gets completed when the connection is fully established.
     */
    public AudioConnectionImpl(ServerVoiceChannel channel, CompletableFuture<AudioConnection> readyFuture) {
        this.channel = channel;
        this.readyFuture = readyFuture;
        id = idCounter.getAndIncrement();
        api = (DiscordApiImpl) channel.getApi();
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(channel.getServer(), getChannel(), false, false);
    }

    /**
     * Gets a future that finishes once the connection is fully established.
     *
     * @return The future.
     */
    public CompletableFuture<AudioConnection> getReadyFuture() {
        return readyFuture;
    }

    /**
     * Gets a future that finishes once the connection has been disconnected.
     *
     * @return The future.
     */
    public CompletableFuture<Void> getDisconnectFuture() {
        return disconnectFuture;
    }

    /**
     * Gets the session id of the audio connection.
     *
     * @return The session id of the audio connection.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Gets the token for the audio connection.
     *
     * @return The token for the audio connection.
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the endpoint for the audio websocket.
     *
     * @return The endpoint for the audio websocket.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the channel of the connection.
     *
     * @param channel The channel of the connection.
     */
    public void setChannel(ServerVoiceChannel channel) {
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
     * Sets the current speaking mode.
     *
     * @param speaking The speaking mode to set
     */
    public void setSpeaking(boolean speaking) {
        EnumSet<SpeakingFlag> newSpeakingFlags = speakingFlags.clone();
        if (speaking) {
            newSpeakingFlags.add(SpeakingFlag.SPEAKING);
        } else {
            newSpeakingFlags.remove(SpeakingFlag.SPEAKING);
        }
        setSpeakingFlags(newSpeakingFlags);
    }

    /**
     * Gets whether the connection is currently speaking with priority.
     *
     * @return Whether the connection is currently speaking with priority.
     */
    public boolean isPrioritySpeaking() {
        return speakingFlags.contains(SpeakingFlag.PRIORITY_SPEAKER);
    }

    /**
     * Sets whether the connection is priority speaking.
     *
     * @param prioritySpeaking Whether or not to speak with priority.
     */
    public void setPrioritySpeaking(boolean prioritySpeaking) {
        EnumSet<SpeakingFlag> newSpeakingFlags = speakingFlags.clone();
        if (prioritySpeaking) {
            newSpeakingFlags.add(SpeakingFlag.PRIORITY_SPEAKER);
        } else {
            newSpeakingFlags.remove(SpeakingFlag.PRIORITY_SPEAKER);
        }
        setSpeakingFlags(newSpeakingFlags);
    }

    /**
     * Gets the current set of active speaking flags.
     *
     * @return The current set of active speaking flags.
     */
    public Set<SpeakingFlag> getSpeakingFlags() {
        return Collections.unmodifiableSet(speakingFlags);
    }

    /**M
     * Sets the current speaking flags and sends a speaking packet if they have changed.
     *
     * @param speakingFlags The new speaking flags to set.
     */
    public void setSpeakingFlags(EnumSet<SpeakingFlag> speakingFlags) {
        if (!speakingFlags.equals(this.speakingFlags)) {
            this.speakingFlags = speakingFlags;
            websocketAdapter.sendSpeaking();
        }
    }

    /**
     * Tries to establish a connection if all required information is available and there's not already a connection.
     *
     * @return Whether it will try to connect or not.
     */
    public synchronized boolean tryConnect() {
        if (movingFuture != null && !movingFuture.isDone()) {
            movingFuture.complete(null);
            return true;
        }
        if (connectingOrConnected || sessionId == null || token == null || endpoint == null) {
            return false;
        }
        connectingOrConnected = true;
        logger.debug("Received all information required to connect to voice channel {}", getChannel());
        websocketAdapter = new AudioWebSocketAdapter(this);
        channel = channel.getCurrentCachedInstance().orElse(channel);
        return true;
    }

    /**
     * Performs a full reconnect of the audio connection by sending a new voice state update.
     */
    public void reconnect() {
        websocketAdapter = null;
        sessionId = null;
        token = null;
        endpoint = null;
        connectingOrConnected = false;
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(getChannel().getServer(), getChannel(), isSelfMuted(), isSelfDeafened());
    }

    /**
     * Gets the current audio source, blocking the thread until it is available.
     *
     * @return The current audio source.
     * @throws InterruptedException If interrupted while waiting.
     */
    public AudioSource getCurrentAudioSourceBlocking() throws InterruptedException {
        return currentSource.get();
    }

    /**
     * Gets the current audio source, waiting up to the specified wait time
     * if necessary for an audio source to become available.
     *
     * @param timeout How long to wait before giving up.
     * @param unit    A {@code TimeUnit} determining how to interpret the {@code timeout} parameter.
     * @return The current audio source, or {@code null} if the specified waiting time elapsed.
     * @throws InterruptedException If interrupted while waiting.
     */
    public AudioSource getCurrentAudioSourceBlocking(long timeout, TimeUnit unit) throws InterruptedException {
        return currentSource.get(timeout, unit);
    }

    @Override
    public DiscordApi getApi() {
        return getChannel().getApi();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public CompletableFuture<Void> moveTo(ServerVoiceChannel destChannel) {
        return moveTo(destChannel, muted, deafened);
    }

    @Override
    public CompletableFuture<Void> moveTo(ServerVoiceChannel destChannel, boolean selfMute, boolean selfDeafen) {
        movingFuture = new CompletableFuture<>();
        if (!destChannel.getServer().equals(getChannel().getServer())) {
            movingFuture.completeExceptionally(
                    new IllegalArgumentException("Cannot move to a voice channel not in the same server!"));
            return movingFuture;
        }
        if (destChannel.equals(getChannel())) {
            movingFuture.complete(null);
            return movingFuture;
        }
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(getChannel().getServer(), destChannel, selfMute, selfDeafen);
        return movingFuture.thenRun(() -> setChannel(destChannel));
    }

    @Override
    public CompletableFuture<Void> close() {
        disconnectFuture = new CompletableFuture<>();
        websocketAdapter.disconnect();
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(getChannel().getServer(), null, muted, deafened);
        ((ServerImpl) getChannel().getServer()).removeAudioConnection(this);
        return disconnectFuture;
    }

    @Override
    public Optional<AudioSource> getAudioSource() {
        try {
            return Optional.ofNullable(currentSource.get(0, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setAudioSource(AudioSource source) {
        currentSource.set(source);
    }

    @Override
    public void removeAudioSource() {
        currentSource.set(null);
    }

    @Override
    public ServerVoiceChannel getChannel() {
        return channel.getCurrentCachedInstance().orElse(channel);
    }

    @Override
    public boolean isSelfMuted() {
        return muted;
    }

    @Override
    public void setSelfMuted(boolean muted) {
        this.muted = muted;
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(getChannel().getServer(), getChannel(), muted, deafened);
    }

    @Override
    public boolean isSelfDeafened() {
        return deafened;
    }

    @Override
    public void setSelfDeafened(boolean deafened) {
        this.deafened = deafened;
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(getChannel().getServer(), getChannel(), muted, deafened);
    }

    @Override
    public String toString() {
        return String.format(
                "AudioConnection (channel: %#s, sessionId: %s, endpoint: %s)", getChannel(), sessionId, endpoint);
    }
}
