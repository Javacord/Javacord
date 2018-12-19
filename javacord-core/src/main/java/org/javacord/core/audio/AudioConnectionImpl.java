package org.javacord.core.audio;

import org.apache.logging.log4j.Logger;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.gateway.AudioWebSocketAdapter;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class AudioConnectionImpl implements AudioConnection {

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
    private final ServerVoiceChannel channel;

    /**
     * A lock to ensure we don't accidentally poll two elements from the queue.
     */
    private final ReentrantLock currentSourceLock = new ReentrantLock();

    /**
     * A future that finishes once the connection is fully established.
     */
    private final CompletableFuture<AudioConnection> readyFuture;

    /**
     * The source that gets played at the moment.
     */
    private volatile AtomicReference<AudioSource> currentSource = new AtomicReference<>();

    /**
     * A queue with all audio sources for this connection.
     */
    private final BlockingQueue<AudioSource> queue = new LinkedBlockingQueue<>();

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
        AudioSource source = null;
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
    public void close() {
        websocketAdapter.disconnect();
        api.getWebSocketAdapter()
                .sendVoiceStateUpdate(channel.getServer(), null, muted, deafened);
        ((ServerImpl) channel.getServer()).setAudioConnection(null);
        ((ServerImpl) channel.getServer()).setPendingAudioConnection(null);
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
