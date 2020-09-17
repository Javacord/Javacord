package org.javacord.core.event.audio;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.event.audio.AudioSourceEvent;

/**
 * The implementation of {@link AudioSourceEvent}.
 */
public abstract class AudioSourceEventImpl implements AudioSourceEvent {

    private final AudioSource source;
    private final AudioConnection connection;

    /**
     * Creates a new audio source event.
     *
     * @param source The audio source of the event.
     * @param connection The audio connection of the event.
     */
    public AudioSourceEventImpl(AudioSource source, AudioConnection connection) {
        this.source = source;
        this.connection = connection;
    }

    @Override
    public AudioSource getSource() {
        return source;
    }

    @Override
    public AudioConnection getConnection() {
        return connection;
    }

    @Override
    public DiscordApi getApi() {
        return connection.getChannel().getApi();
    }
}
