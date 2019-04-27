package org.javacord.api.audio.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.audio.AudioSourceAttachableListener;
import org.javacord.api.listener.audio.AudioSourceAttachableListenerManager;
import org.javacord.api.util.event.ListenerManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ListenerManager} specifically for managing Listeners attached to custom audio sources.
 *
 * @param <T> The type of listener.
 */
public class AudioListenerManager<T extends AudioSourceAttachableListener & ObjectAttachableListener>
        implements ListenerManager<T> {

    /**
     * The api for scheduling purposes.
     */
    private final DiscordApi api;

    /**
     * The listener.
     */
    private final T listener;

    /**
     * The type the listener got registered as.
     */
    private final Class<T> listenerClass;

    /**
     * The object the listener was assigned to.
     */
    private final AudioSourceAttachableListenerManager assignedObject;

    /**
     * The handlers to be called when the listener is removed.
     */
    private final List<Runnable> removeHandlers = new CopyOnWriteArrayList<>();

    /**
     * Create a listener manager for an audio related listener attached to an object.
     * @param api The discord api instance.
     * @param listener The listener.
     * @param listenerClass The listener class.
     * @param assignedObject The object the listener is attached to.
     */
    public AudioListenerManager(DiscordApi api, T listener, Class<T> listenerClass,
                                AudioSourceAttachableListenerManager assignedObject) {
        this.api = api;
        this.listener = listener;
        this.listenerClass = listenerClass;
        this.assignedObject = assignedObject;
    }

    @Override
    public boolean isGlobalListener() {
        return false;
    }

    @Override
    public Class<T> getListenerClass() {
        return listenerClass;
    }

    @Override
    public T getListener() {
        return listener;
    }

    @Override
    public Optional<Class<?>> getAssignedObjectClass() {
        return Optional.of(AudioSource.class);
    }

    @Override
    public Optional<Long> getAssignedObjectId() {
        return Optional.empty();
    }

    @Override
    public ListenerManager<T> remove() {
        assignedObject.removeListener(listenerClass, listener);
        // Remove listeners will be called from the assigned object.
        return this;
    }

    /**
     * Calls the remove handlers after the listener has been removed.
     */
    public void callRemoveHandlers() {
        removeHandlers.forEach(Runnable::run);
    }

    @Override
    public ListenerManager<T> removeAfter(long delay, TimeUnit timeUnit) {
        if (delay > 0) {
            this.api.getThreadPool().getScheduler().schedule(this::remove, delay, timeUnit);
        } else {
            this.remove();
        }
        return this;
    }

    @Override
    public void addRemoveHandler(Runnable removeHandler) {
        removeHandlers.add(removeHandler);
    }
}
