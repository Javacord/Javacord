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
     * Gets the server of the audio connection.
     *
     * @return The server of the audio connection.
     */
    default Server getServer() {
        return getChannel().getServer();
    }

}
