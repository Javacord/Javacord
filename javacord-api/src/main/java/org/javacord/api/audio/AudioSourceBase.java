package org.javacord.api.audio;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.internal.AudioSourceBaseDelegate;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.audio.AudioSourceAttachableListener;
import org.javacord.api.listener.audio.AudioSourceFinishedListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The base class of every audio source.
 *
 * <p>It already implements all methods required for listener handling, muting and the {@link #hasFinished()} method.
 */
public abstract class AudioSourceBase implements AudioSource {

    /**
     * The server voice channel delegate used by this instance.
     */
    private final AudioSourceBaseDelegate delegate;

    /**
     * A list with all transformers of the audio source.
     */
    private final List<AudioTransformer> transformers = Collections.synchronizedList(new ArrayList<>());

    /**
     * If the audio source is muted.
     */
    private volatile boolean muted = false;

    /**
     * Creates a new audio source base.
     *
     * @param api The discord api instance.
     */
    public AudioSourceBase(DiscordApi api) {
        if (api == null) {
            throw new IllegalArgumentException("api must not be null!");
        }
        delegate = DelegateFactory.createAudioSourceBaseDelegate(api);
    }

    /**
     * Gets the delegate used by this audio source internally.
     *
     * @return The delegate used by this audio source internally.
     */
    public AudioSourceBaseDelegate getDelegate() {
        return delegate;
    }

    /**
     * Transforms the given frame using the audio source's transformers.
     *
     * @param frame The frame to transform.
     * @return The transformed frame.
     */
    protected final byte[] applyTransformers(byte[] frame) {
        if (frame == null) {
            return null;
        }
        synchronized (transformers) {
            for (AudioTransformer transformer : transformers) {
                frame = transformer.transform(this, frame);
            }
        }
        return frame;
    }

    @Override
    public final DiscordApi getApi() {
        return delegate.getApi();
    }

    @Override
    public final void addTransformer(AudioTransformer transformer) {
        transformers.add(transformer);
    }

    @Override
    public final boolean removeTransformer(AudioTransformer transformer) {
        return transformers.remove(transformer);
    }

    @Override
    public final List<AudioTransformer> getTransformers() {
        return Collections.unmodifiableList(new ArrayList<>(transformers));
    }

    @Override
    public final void removeTransformers() {
        transformers.clear();
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    @Override
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    @Override
    public boolean hasFinished() {
        // In most cases the audio source has finished when no more frames are available.
        // This does not always have to be the case though, e.g. if it's streaming audio
        // from the internet and wasn't able to fetch the next frame in time. In this case
        // the audio source has no next frame but is not finished!
        return !hasNextFrame();
    }

    @Override
    public final ListenerManager<AudioSourceFinishedListener>
            addAudioSourceFinishedListener(AudioSourceFinishedListener listener) {
        return delegate.addAudioSourceFinishedListener(listener);
    }

    @Override
    public final List<AudioSourceFinishedListener> getAudioSourceFinishedListeners() {
        return delegate.getAudioSourceFinishedListeners();
    }

    @Override
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addAudioSourceAttachableListener(T listener) {
        return delegate.addAudioSourceAttachableListener(listener);
    }

    @Override
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> void
            removeAudioSourceAttachableListener(T listener) {
        delegate.removeAudioSourceAttachableListener(listener);
    }

    @Override
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getAudioSourceAttachableListeners() {
        return delegate.getAudioSourceAttachableListeners();
    }

    @Override
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> void
            removeListener(Class<T> listenerClass, T listener) {
        delegate.removeListener(listenerClass, listener);
    }
}
