package org.javacord.api.audio;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;

import java.util.Optional;

public interface AudioConnection {

    /**
     * Queues the audio source.
     *
     * @param source The audio source to queue.
     */
    void queue(AudioSource source);

    /**
     * Dequeues the given audio source.
     *
     * @param source The audio source to dequeue if present.
     * @return If this queue changed as a result of the call.
     */
    boolean dequeue(AudioSource source);

    /**
     * Dequeues the current audio source if present.
     *
     * @return If this queue changed as a result of the call.
     */
    default boolean dequeueCurrentSource() {
        return getCurrentAudioSource().map(this::dequeue).orElse(false);
    }

    /**
     * Disconnects from the voice channel.
     *
     * <p><b>A disconnected audio source cannot be reused!</b>
     */
    void close();

    /**
     * Gets the current audio source.
     *
     * @return The current audio source.
     */
    Optional<AudioSource> getCurrentAudioSource();

    /**
     * Gets the voice channel of the audio connection.
     *
     * @return The voice channel of the audio connection.
     */
    ServerVoiceChannel getChannel();

    /**
     * Gets the self-muted status of this connection.
     *
     * <p>This is solely visual. A muted bot can still play audio.
     *
     * @return Whether or not the connection is self-muted.
     */
    boolean isSelfMuted();

    /**
     * Sets the self-muted status of this connection.
     *
     * <p>This is solely visual. A muted bot can still play audio.
     *
     * @param muted Whether or not to self-mute this connection.
     */
    void setSelfMuted(boolean muted);

    /**
     * Gets the self-deafened status of this connection.
     *
     * <p>This is solely visual. A deafened bot can still receive audio.
     *
     * @return Whether or not the connection is self-deafened.
     */
    boolean isSelfDeafened();

    /**
     * Sets the self-deafened status of this connection.
     *
     * <p>This is solely visual. A deafened bot can still receive audio.
     *
     * @param deafened Whether or not to self-deafen this connection.
     */
    void setSelfDeafened(boolean deafened);

    /**
     * Gets the server of the audio connection.
     *
     * @return The server of the audio connection.
     */
    default Server getServer() {
        return getChannel().getServer();
    }

}
