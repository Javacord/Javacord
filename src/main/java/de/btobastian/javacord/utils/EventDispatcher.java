package de.btobastian.javacord.utils;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is used to dispatch events.
 */
public class EventDispatcher {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EventDispatcher.class);

    /**
     * The time which a listener task is allowed to take until it get's interrupted.
     */
    private static final int MAX_EXECUTION_TIME_IN_SECONDS = 2*60; // 2 minutes

    /**
     * The time which a listener task is allowed to take until a warning appears on INFO log level.
     */
    private static final int INFO_WARNING_DELAY_IN_SECONDS = 10; // 10 seconds

    /**
     * The time which a listener task is allowed to take until a warning appears on DEBUG log level.
     */
    private static final int DEBUG_WARNING_DELAY_IN_MILLIS = 500; // 500 milliseconds

    /**
     * The last time a warning was sent on DEBUG log level.
     */
    private volatile long lastDebugWarning = System.nanoTime() - 60*60*1_000_000_000L; // 1 hour ago

    /**
     * The last time a warning was sent on INFO log level.
     */
    private volatile long lastInfoWarning = System.nanoTime() - 60*60*1_000_000_000L; // 1 hour ago

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * This map which holds a queue for every object (usually a server) with tasks to call the waiting listeners.
     */
    private final ConcurrentHashMap<Object, Queue<Runnable>> queuedListenerTasks = new ConcurrentHashMap<>();

    /**
     * A list with all objects which currently have a running listener.
     */
    private final List<Object> runningListeners = new ArrayList<>();

    /**
     * A map with all running listeners as it's key. The value contains an array where the first element is a long
     * with the start time of the listener (using {@link System#nanoTime()} and the second element is the object
     * of the listener (usually a server).
     */
    private final ConcurrentHashMap<Future<?>, Object[]> activeListeners = new ConcurrentHashMap<>();

    /**
     * Creates a new event dispatcher.
     *
     * @param api The discord api instance.
     */
    public EventDispatcher(DiscordApi api) {
        this.api = api;
        api.getThreadPool().getScheduler().scheduleAtFixedRate(() -> {
            List<Object> canceledObjects = new ArrayList<>();
            synchronized (activeListeners) {
                long currentNanoTime = System.nanoTime();
                Iterator<Map.Entry<Future<?>, Object[]>> iterator = activeListeners.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Future<?>, Object[]> entry = iterator.next();
                    long difference = currentNanoTime - ((long) entry.getValue()[0]);
                    if (difference > DEBUG_WARNING_DELAY_IN_MILLIS * 1_000_000L &&
                            difference < DEBUG_WARNING_DELAY_IN_MILLIS * 1_000_000L + 201_000_000L) {
                        // Only log this message once every 20 seconds
                        if (currentNanoTime - lastDebugWarning > 21*1_000_000_000L) {
                            logger.debug("Detected a {} which is now running for over {}ms ({}ms). This is" +
                                            " an unusual long time for a listener task. Make sure to not do" +
                                            " any heavy computations in listener threads!",
                                    entry.getValue()[1] instanceof DiscordApi ? "global listener thread" :
                                            String.format("listener thread for %s", entry.getValue()[1]),
                                    DEBUG_WARNING_DELAY_IN_MILLIS, (int) (difference/1_000_000L));
                            lastDebugWarning = currentNanoTime;
                        }
                    }
                    if (difference > INFO_WARNING_DELAY_IN_SECONDS * 1_000_000_000L &&
                            difference < INFO_WARNING_DELAY_IN_SECONDS * 1_000_000_000L + 201_000_000L) {
                        // Only log this message once every 1 minute
                        if (currentNanoTime - lastInfoWarning > 61*1_000_000_000L) {
                            logger.warn("Detected a {} which is now running for over {} seconds ({}ms)." +
                                            " This is a very unusual long time for a listener task. Make sure to not do" +
                                            " any heavy computations in listener threads!",
                                    entry.getValue()[1] instanceof DiscordApi ? "global listener thread" :
                                            String.format("listener thread for %s", entry.getValue()[1]),
                                    INFO_WARNING_DELAY_IN_SECONDS, (int) (difference/1_000_000L));
                            lastInfoWarning = currentNanoTime;
                        }
                    }
                    if (difference > MAX_EXECUTION_TIME_IN_SECONDS * 1_000_000_000L) {
                        entry.getKey().cancel(true);
                        logger.error("Interrupted a {}, because it was running over {} seconds! This was most likely" +
                                " caused by a deadlock or very heavy computation/blocking operations in the listener" +
                                " thread. Make sure to not block listener threads!",
                                entry.getValue()[1] instanceof DiscordApi ? "global listener thread" :
                                        String.format("listener thread for %s", entry.getValue()[1]),
                                MAX_EXECUTION_TIME_IN_SECONDS);
                        synchronized (runningListeners) {
                            activeListeners.remove(entry.getKey());
                            iterator.remove();
                        }
                        canceledObjects.add(entry.getValue()[1]);
                    }
                }
            }
            canceledObjects.forEach(this::checkRunningListenersAndStartIfPossible);
        }, 200, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * Dispatches an event to the given listeners using the provided consumer.
     * Calling this method usually looks like this:
     * <code>dispatchEvent(server, listeners, listener -> listener.onXyz(event));</code>
     *
     * @param object The object which is used to determine in which thread the consumer should be executed.
     *               Usually the object is a server object (for server-dependent events), the discord api instance
     *               (for server-independent events) or <code>null</code>. Providing a <code>null</code> parameter
     *               means, that all previously consumers are going to run and then the new consumers. When
     *               all null-parameter consumers were executed it begins to execute the other consumers like normal
     *               again.
     * @param listeners A list with listeners which get consumed by the given consumer.
     * @param consumer A consumer which consumes all listeners from the given list and is meant to call their
     *                 <code>onXyz(Event)</code> method.
     * @param <T> The type of the listener.
     */
    public <T> void dispatchEvent(Object object, List<T> listeners, Consumer<T> consumer) {
        // TODO handle null object for stuff like the LostConnectionListener
        synchronized (queuedListenerTasks) {
            Queue<Runnable> queue = queuedListenerTasks.computeIfAbsent(object, o -> new LinkedList<>());
            listeners.forEach(listener -> queue.add(() -> consumer.accept(listener)));
        }
        checkRunningListenersAndStartIfPossible(object);
    }

    /**
     * Checks if there are listeners running for the given object and if
     * not it takes one from the queue for the object and executes it.
     *
     * @param object The object which is used to determine in which thread the consumer should be executed.
     */
    private void checkRunningListenersAndStartIfPossible(Object object) {
        synchronized (queuedListenerTasks) {
            Queue<Runnable> queue = queuedListenerTasks.get(object);
            if (queue == null) {
                return;
            }
            synchronized (runningListeners) {
                if (!runningListeners.contains(object) && !queue.isEmpty()) {
                    runningListeners.add(object);
                    // Yes, an array is a hacky solution, but whatever works ;-)
                    // Because of the synchronization on activeListener we can be sure, that activeListener[0] gets
                    // initialized before being accessed inside the runnable.
                    Future<?>[] activeListener = new Future<?>[1];
                    // Call the listener
                    synchronized (activeListener) {
                        activeListener[0] = api.getThreadPool().getExecutorService().submit(() -> {
                            synchronized (activeListeners) {
                                synchronized (activeListener) {
                                    // Add the future to the list of active listeners
                                    activeListeners.put(activeListener[0], new Object[]{System.nanoTime(), object});
                                }
                            }
                            try {
                                queue.poll().run();
                            } catch (Throwable t) {
                                logger.error("Unhandled exception in {}!", object instanceof DiscordApi ?
                                        "global listener thread" : String.format("listener thread for %s", object), t);
                            }
                            synchronized (activeListeners) {
                                synchronized (runningListeners) {
                                    if (!Thread.interrupted()) {
                                        activeListeners.remove(activeListener[0]);
                                        runningListeners.remove(object);
                                    }
                                }
                            }
                            checkRunningListenersAndStartIfPossible(object);
                        });
                    }

                }
            }
        }
    }

}
