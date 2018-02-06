package de.btobastian.javacord.utils;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This class can be used to edit added listeners afterwards.
 *
 * @param <T> The listener class.
 */
public class ListenerManager<T> {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

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
    public ListenerManager(DiscordApi api, T listener, Class<T> listenerClass) {
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
    public ListenerManager(
            DiscordApi api, T listener, Class<T> listenerClass, Class<?> assignedObjectClass, long objectId) {
        this.api = (ImplDiscordApi) api;
        this.listener = listener;
        this.listenerClass = listenerClass;
        this.assignedObjectClass = assignedObjectClass;
        this.objectId = objectId;
    }

    /**
     * Checks if the managed listener is a global listener.
     *
     * @return Whether the managed listener is a global listener or not.
     */
    public boolean isGlobalListener() {
        return assignedObjectClass == null;
    }

    /**
     * Gets the managed listener.
     *
     * @return The managed listener.
     */
    public T getListener() {
        return listener;
    }

    /**
     * Gets the class of the object, the listener was added to.
     * For global listeners, it returns an empty {@code Optional}.
     *
     * @return The class of the object, the listener was added to.
     */
    public Optional<Class<?>> getAssignedObjectClass() {
        return Optional.ofNullable(assignedObjectClass);
    }

    /**
     * Gets the id of the object, the listener as added to.
     * Empty for global listeners.
     *
     * @return The id of the object, the listener was added to.
     */
    public Optional<Long> getAssignedObjectId() {
        if (isGlobalListener()) {
            return Optional.empty();
        }
        return Optional.of(objectId);
    }

    /**
     * Called when the listener is removed.
     */
    public void removed() {
        removeHandlers.forEach(Runnable::run);
    }

    /**
     * Removes the listener.
     *
     * @return The current instance in order to chain call methods.
     */
    public ListenerManager<T> remove() {
        if (isGlobalListener()) {
            api.removeListener(listenerClass, listener);
        } else {
            api.removeObjectListener(assignedObjectClass, objectId, listenerClass, listener);
        }
        return this;
    }

    /**
     * Removes the listener after the given delay.
     *
     * @param delay The time to wait before removing the listener.
     * @param timeUnit The time unit of the delay.
     * @return The current instance in order to chain call methods.
     */
    public ListenerManager<T> removeAfter(long delay, TimeUnit timeUnit) {
        api.getThreadPool().getScheduler().schedule((Runnable) this::remove, delay, timeUnit);
        return this;
    }

    /**
     * Adds a runnable which gets called when the listener gets removed.
     *
     * @param removeHandler The handler which gets called when the listener gets remove.
     */
    public void addRemoveHandler(Runnable removeHandler) {
        removeHandlers.add(removeHandler);
    }

}
