package org.javacord.api.audio;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.internal.AudioListenerManager;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.audio.AudioSourceAttachableListener;
import org.javacord.api.listener.audio.AudioSourceFinishedListener;
import org.javacord.api.util.event.ListenerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * A base class for audio sources.
 *
 * <p>It already implements all methods required for listener handling, muting and the {@link #hasFinished()} method.
 */
public abstract class AudioSourceBase implements AudioSource {

    /**
     * The api instance for this audio source.
     */
    private final DiscordApi api;

    /**
     * A list with all transformers of the audio source.
     */
    private final List<AudioTransformer> transformers = Collections.synchronizedList(new ArrayList<>());

    /**
     * The directly stored listeners.
     */
    private final Map<Class<? extends AudioSourceAttachableListener>, List<? extends AudioSourceAttachableListener>>
            listeners = new ConcurrentHashMap<>();
    private final Map<? extends AudioSourceAttachableListener, Collection<AudioListenerManager<?>>> managers
            = new ConcurrentHashMap<>();

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
        this.api = Objects.requireNonNull(api);
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
        return api;
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

    // Required because Object#clone() is protected by default.
    // Without this abstract method, you cannot compile the class.
    @Override
    public abstract AudioSource clone();

    @Override
    public final ListenerManager<AudioSourceFinishedListener>
            addAudioSourceFinishedListener(AudioSourceFinishedListener listener) {
        return addListener(AudioSourceFinishedListener.class, listener);
    }

    @Override
    public final List<AudioSourceFinishedListener> getAudioSourceFinishedListeners() {
        synchronized (listeners) {
            return getListeners(AudioSourceFinishedListener.class);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addAudioSourceAttachableListener(T listener) {
        // Find the listener types we need to register this as.
        List<Class<T>> interfaces = new ArrayList<>();
        List<Class<?>> classesToCheck = new ArrayList<>();
        classesToCheck.add(listener.getClass());
        while (classesToCheck.size() > 0) {
            classesToCheck.stream()
                    .flatMap(interfaceClass -> Arrays.stream(interfaceClass.getInterfaces()))
                    .filter(AudioSourceAttachableListener.class::isAssignableFrom)
                    .filter(ObjectAttachableListener.class::isAssignableFrom)
                    .map(interfaceClass -> (Class<T>) interfaceClass)
                    .forEach(interfaces::add);

            List<Class<?>> classesToCheckNextPass = new ArrayList<>();
            classesToCheck.forEach(classToCheck -> {
                if (classToCheck.getSuperclass() != null) {
                    classesToCheckNextPass.add(classToCheck.getSuperclass());
                }
                Collections.addAll(classesToCheckNextPass, classToCheck.getInterfaces());
            });
            classesToCheck = classesToCheckNextPass;
        }
        return interfaces.stream()
                .distinct()
                .map(listenerClass -> addListener(listenerClass, listenerClass.cast(listener)))
                .collect(Collectors.toList());
    }

    @Override
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> void
            removeAudioSourceAttachableListener(T listener) {
        listeners.values().forEach(list -> list.remove(listener));
        managers.remove(listener).forEach(AudioListenerManager::callRemoveHandlers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getAudioSourceAttachableListeners() {
        Map<T, List<Class<T>>> listenerToClass = new ConcurrentHashMap<>();
        managers.forEach((listener, managers) ->
                listenerToClass.put((T) listener,
                        managers.stream()
                                .map(manager -> (ListenerManager<T>) manager)
                                .map(ListenerManager::getListenerClass)
                                .collect(Collectors.toList())
                )
        );
        return listenerToClass;
    }


    @SuppressWarnings("unchecked")
    private <T extends AudioSourceAttachableListener & ObjectAttachableListener> ListenerManager<T> addListener(
            Class<T> listenerType, T listener) {
        synchronized (listeners) {
            if (!listeners.containsKey(listenerType)) {
                listeners.put(listenerType, new CopyOnWriteArrayList<T>());
            }
            ((List<T>)listeners.get(listenerType)).add(listener);
        }
        return new AudioListenerManager<>(api, listener, listenerType, this);
    }

    @SuppressWarnings("unchecked")
    private  <T extends AudioSourceAttachableListener & ObjectAttachableListener> List<T> getListeners(
            Class<T> listenerClass) {
        synchronized (listeners) {
            if (listeners.containsKey(listenerClass)) {
                return Collections.unmodifiableList((List<T>)listeners.get(listenerClass));
            } else {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public final <T extends AudioSourceAttachableListener & ObjectAttachableListener> void
            removeListener(Class<T> listenerClass, T listener) {
        synchronized (listeners) {
            if (listeners.containsKey(listenerClass)) {
                listeners.get(listenerClass).remove(listener);
                managers.remove(listener).stream()
                        .filter(manager -> manager.getListenerClass().equals(listenerClass))
                        .forEach(AudioListenerManager::callRemoveHandlers);
            }
        }
    }
}
