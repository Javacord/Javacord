package org.javacord.core.audio;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.AudioTransformer;
import org.javacord.api.audio.internal.AudioSourceBaseDelegate;
import org.javacord.core.listener.audio.InternalAudioSourceAttachableListenerManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The implementation of {@link AudioSourceBaseDelegate}.
 */
public class AudioSourceBaseDelegateImpl implements
        AudioSourceBaseDelegate, InternalAudioSourceAttachableListenerManager {

    /**
     * An internal counter for the audio source id.
     */
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    /**
     * An artificial id for the connection.
     */
    private final long id;

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * Creates a new audio source base delegate.
     *
     * @param api The discord api instance.
     */
    public AudioSourceBaseDelegateImpl(DiscordApi api) {
        this.api = api;
        id = idCounter.getAndIncrement();
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void addTransformer(AudioTransformer transformer) {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public boolean removeTransformer(AudioTransformer transformer) {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public List<AudioTransformer> getTransformers() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public void removeTransformers() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public byte[] getNextFrame() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public boolean hasNextFrame() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public boolean hasFinished() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public boolean isMuted() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public void setMuted(boolean muted) {
        throw new UnsupportedOperationException("Not supported in delegate");
    }

    @Override
    public AudioSource copy() {
        throw new UnsupportedOperationException("Not supported in delegate");
    }
}
