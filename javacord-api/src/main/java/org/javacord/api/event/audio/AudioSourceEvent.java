package org.javacord.api.event.audio;

import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.Event;

/**
 * An audio source event.
 */
public interface AudioSourceEvent extends Event {

    /**
     * Gets the audio source of the event.
     *
     * @return The audio source of the event.
     */
    AudioSource getSource();

    /**
     * Gets the audio connection of the event.
     *
     * @return The audio connection of the event.
     */
    AudioConnection getConnection();

    /**
     * Gets the server voice channel of the event.
     *
     * @return The server voice channel of the event.
     */
    default ServerVoiceChannel getChannel() {
        return getConnection().getChannel();
    }

    /**
     * Gets the server of the event.
     *
     * @return The server of the event.
     */
    default Server getServer() {
        return getConnection().getServer();
    }

}
