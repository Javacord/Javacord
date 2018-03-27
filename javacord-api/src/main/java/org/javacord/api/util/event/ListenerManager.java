package org.javacord.api.util.event;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This class can be used to edit added listeners afterwards.
 *
 * @param <T> The listener class.
 */
public interface ListenerManager<T> {

    /**
     * Checks if the managed listener is a global listener.
     *
     * @return Whether the managed listener is a global listener or not.
     */
    boolean isGlobalListener();

    /**
     * Gets the listener class context for the managed listener.
     *
     * @return The listener class context for the managed listener.
     */
    Class<T> getListenerClass();

    /**
     * Gets the managed listener.
     *
     * @return The managed listener.
     */
    T getListener();

    /**
     * Gets the class of the object, the listener was added to.
     * For global listeners, it returns an empty {@code Optional}.
     *
     * @return The class of the object, the listener was added to.
     */
    Optional<Class<?>> getAssignedObjectClass();

    /**
     * Gets the id of the object, the listener as added to.
     * Empty for global listeners.
     *
     * @return The id of the object, the listener was added to.
     */
    Optional<Long> getAssignedObjectId();

    /**
     * Removes the listener.
     *
     * @return The current instance in order to chain call methods.
     */
    ListenerManager<T> remove();

    /**
     * Removes the listener after the given delay.
     *
     * @param delay The time to wait before removing the listener.
     * @param timeUnit The time unit of the delay.
     * @return The current instance in order to chain call methods.
     */
    ListenerManager<T> removeAfter(long delay, TimeUnit timeUnit);

    /**
     * Adds a runnable which gets called when the listener gets removed.
     *
     * @param removeHandler The handler which gets called when the listener gets remove.
     */
    void addRemoveHandler(Runnable removeHandler);

}
