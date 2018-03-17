package org.javacord.core.util.event;

import org.javacord.api.DiscordApi;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This implementation of {@link ListenerManager}.
 *
 * @param <T> The listener class.
 */
public class ListenerManagerImpl<T> implements ListenerManager<T> {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The managed listener.
     */
    private final T listener;

    /**
     * The class of the listener to manage.
     */
    private final Class<T> listenerClass;

    /**
     * The class of the object, the listener was added to.
     * <code>Null</code> if it's a global listener.
     */
    private final Class<?> assignedObjectClass;

    /**
     * The id of the object, the listener was added to.
     * <code>-1</code> if it's a global listener.
     */
    private final long objectId;

    /**
     * The remove handlers.
     */
    private final List<Runnable> removeHandlers = new ArrayList<>();

    /**
     * Creates a new listener manager for a global listener.
     *
     * @param api The api instance.
     * @param listener The listener to manage.
     * @param listenerClass The class of the listener to manage.
     */
    public ListenerManagerImpl(DiscordApi api, T listener, Class<T> listenerClass) {
        this(api, listener, listenerClass, null, -1);
    }

    /**
     * Creates a new listener manager.
     *
     * @param api The discord api instance.
     * @param listener The listener to manage.
     * @param listenerClass The class of the listener to manage.
     * @param assignedObjectClass The class of the object, the listener was added to.
     * @param objectId The id of the object, the listener was added to.
     */
    public ListenerManagerImpl(
            DiscordApi api, T listener, Class<T> listenerClass, Class<?> assignedObjectClass, long objectId) {
        this.api = (DiscordApiImpl) api;
        this.listener = listener;
        this.listenerClass = listenerClass;
        this.assignedObjectClass = assignedObjectClass;
        this.objectId = objectId;
    }

    /**
     * Called when the listener is removed.
     */
    public void removed() {
        removeHandlers.forEach(Runnable::run);
    }

    @Override
    public boolean isGlobalListener() {
        return assignedObjectClass == null;
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
        return Optional.ofNullable(assignedObjectClass);
    }

    @Override
    public Optional<Long> getAssignedObjectId() {
        if (isGlobalListener()) {
            return Optional.empty();
        }
        return Optional.of(objectId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ListenerManagerImpl<T> remove() {
        if (isGlobalListener()) {
            api.removeListener(
                    (Class<GloballyAttachableListener>) listenerClass, (GloballyAttachableListener) listener);
        } else {
            api.removeObjectListener(
                    assignedObjectClass, objectId, (Class<ObjectAttachableListener>) listenerClass,
                    (ObjectAttachableListener) listener);
        }
        return this;
    }

    @Override
    public ListenerManagerImpl<T> removeAfter(long delay, TimeUnit timeUnit) {
        api.getThreadPool().getScheduler().schedule((Runnable) this::remove, delay, timeUnit);
        return this;
    }

    @Override
    public void addRemoveHandler(Runnable removeHandler) {
        removeHandlers.add(removeHandler);
    }

}
