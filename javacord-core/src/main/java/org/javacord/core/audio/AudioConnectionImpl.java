package org.javacord.core.audio;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.audio.InternalAudioConnectionAttachableListenerManager;
import org.javacord.core.util.gateway.AudioWebSocketAdapter;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

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
     * A lock to ensure we don't accidentally poll two elements from the queue.
     */
    private final ReentrantLock currentSourceLock = new ReentrantLock();

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
     * The source that gets played at the moment.
     */
    private volatile AtomicReference<AudioSource> currentSource = new AtomicReference<>();

    /**
     * A queue with all audio sources for this connection.
     */
    private final BlockingQueue<AudioSource> queue = new LinkedBlockingQueue<>();

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
                .sendVoiceStateUpdate(channel.getServer(), channel, false, false);
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
        websocketAdapter.sendSpeaking(speaking);
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
        logger.debug("Received all information required to connect to voice channel {}", channel);
        websocketAdapter = new AudioWebSocketAdapter(this);
        return true;
    }

    /**
     * Gets the current audio source, waiting up to the specified wait time
     * if necessary for an audio source to become available.
     *
     * @param timeout How long to wait before giving up.
     * @param unit A {@code TimeUnit} determining how to interpret the {@code timeout} parameter.
     * @return The current audio source, or {@code null} if the specified waiting time elapsed.
     * @throws InterruptedException If interrupted while waiting.
     */
    public AudioSource getCurrentAudioSourceBlocking(long timeout, TimeUnit unit) throws InterruptedException {
        AudioSource source;
        currentSourceLock.lock();
        try {
            AtomicBoolean poll = new AtomicBoolean(false);
            source = currentSource.updateAndGet(currentSource -> {
                if (currentSource == null) {
                    // Always poll if the current source is null
                    poll.set(true);
                } else {
                    // If the current source is not null, only poll if it's still queued
                    poll.set(queue.peek() == currentSource);
                }
                return currentSource;
            });
            if (poll.get()) {
                AudioSource sourceToSet = queue.poll(timeout, unit);
                if (sourceToSet != null) { // If it's null, it timed out
                    currentSource.set(sourceToSet);
                    source = sourceToSet;
                }
            }
        } finally {
            currentSourceLock.unlock();
        }
        return source;
    }

    /**
     * Removes the current audio source.
     */
    public void removeCurrentSource() {
        currentSource.set(null);
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
    public void queue(AudioSource source) {
        queue.add(source);
    }

    @Override
    public boolean dequeue(AudioSource source) {
        if (currentSource.get() == source) {
            removeCurrentSource();
            return true;
        } else {
            return queue.remove(source);
        }
    }

    @Override
    public CompletableFuture<Void> moveTo(ServerVoiceChannel destChannel) {
        return moveTo(destChannel, muted, deafened);
    }

    @Override
    public CompletableFuture<Void> moveTo(ServerVoiceChannel destChannel, boolean selfMute, boolean selfDeafen) {
        movingFuture = new CompletableFuture<>();
        if (!destChannel.getServer().equals(channel.getServer())) {
            movingFuture.completeExceptionally(
                    new IllegalArgumentException("Cannot move to a voice channel not in the same server!"));
            return movingFuture;
        }
        if (destChannel.equals(channel)) {
            movingFuture.complete(null);
            return movingFuture;
        }
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(channel.getServer(), destChannel, selfMute, selfDeafen);
        return movingFuture.thenRun(() -> setChannel(destChannel));
    }

    @Override
    public CompletableFuture<Void> close() {
        disconnectFuture = new CompletableFuture<>();
        websocketAdapter.disconnect();
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(channel.getServer(), null, muted, deafened);
        ((ServerImpl) channel.getServer()).removeAudioConnection(this);
        return disconnectFuture;
    }

    @Override
    public Optional<AudioSource> getCurrentAudioSource() {
        return Optional.ofNullable(
                currentSource.updateAndGet(source -> {
                    if (source != null) {
                        return source;
                    }
                    return queue.peek();
                }));
    }

    @Override
    public ServerVoiceChannel getChannel() {
        return channel;
    }

    @Override
    public boolean isSelfMuted() {
        return muted;
    }

    @Override
    public void setSelfMuted(boolean muted) {
        this.muted = muted;
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(channel.getServer(), channel, muted, deafened);
    }

    @Override
    public boolean isSelfDeafened() {
        return deafened;
    }

    @Override
    public void setSelfDeafened(boolean deafened) {
        this.deafened = deafened;
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(channel.getServer(), channel, muted, deafened);
    }

    @Override
    public String toString() {
        return String
                .format("AudioConnection (channel: %#s, sessionId: %s, endpoint: %s)", channel, sessionId, endpoint);
    }
}
