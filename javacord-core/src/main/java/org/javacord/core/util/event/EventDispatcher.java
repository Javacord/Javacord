package org.javacord.core.util.event;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.logging.LoggerUtil;

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
    private static final int MAX_EXECUTION_TIME_IN_SECONDS = 2 * 60; // 2 minutes

    /**
     * The time which a listener task is allowed to take until a warning appears on INFO log level.
     */
    private static final int INFO_WARNING_DELAY_IN_SECONDS = 10; // 10 seconds

    /**
     * The time which a listener task is allowed to take until a warning appears on DEBUG log level.
     */
    private static final int DEBUG_WARNING_DELAY_IN_MILLIS = 500; // 500 milliseconds

    /**
     * A dummy object used for object independent tasks.
     */
    private static final Object OBJECT_INDEPENDENT_TASK_INDICATOR = new Object();

    /**
     * Whether execution time checking should be enabled or not.
     */
    private volatile boolean executionTimeCheckingEnabled = true;

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
        queuedListenerTasks.put(OBJECT_INDEPENDENT_TASK_INDICATOR, new LinkedList<>());
        api.getThreadPool().getScheduler().scheduleAtFixedRate(() -> {
            try {
                if (!executionTimeCheckingEnabled) {
                    return;
                }
                List<Object> canceledObjects = new ArrayList<>();
                synchronized (activeListeners) {
                    long currentNanoTime = System.nanoTime();
                    Iterator<Map.Entry<Future<?>, Object[]>> iterator = activeListeners.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Future<?>, Object[]> entry = iterator.next();
                        long difference = currentNanoTime - ((long) entry.getValue()[0]);
                        Object object = entry.getValue()[1];
                        if (difference > DEBUG_WARNING_DELAY_IN_MILLIS * 1_000_000L
                                && difference < DEBUG_WARNING_DELAY_IN_MILLIS * 1_000_000L + 201_000_000L) {
                            logger.debug("Detected a {} which is now running for over {}ms ({}ms). This is"
                                            + " an unusually long execution time for a listener task. Make"
                                            + " sure to not do any heavy computations in listener threads!",
                                    () -> getThreadType(object),
                                    () -> DEBUG_WARNING_DELAY_IN_MILLIS,
                                    () -> (int) (difference / 1_000_000L));
                        }
                        if (difference > INFO_WARNING_DELAY_IN_SECONDS * 1_000_000_000L
                                && difference < INFO_WARNING_DELAY_IN_SECONDS * 1_000_000_000L + 201_000_000L) {
                            logger.warn("Detected a {} which is now running for over {} seconds ({}ms)."
                                            + " This is a very unusually long execution time for a listener task. Make"
                                            + " sure to not do any heavy computations in listener threads!",
                                    () -> getThreadType(object),
                                    () -> INFO_WARNING_DELAY_IN_SECONDS,
                                    () -> (int) (difference / 1_000_000L));
                        }
                        if (difference > MAX_EXECUTION_TIME_IN_SECONDS * 1_000_000_000L) {
                            entry.getKey().cancel(true);
                            logger.error("Interrupted a {}, because it was running over {} seconds! This was most "
                                    + "likely caused by a deadlock or very heavy computation/blocking operations in "
                                    + "the listener thread. Make sure to not block listener threads!",
                                    () -> getThreadType(object), () -> MAX_EXECUTION_TIME_IN_SECONDS);
                            synchronized (runningListeners) {
                                runningListeners.remove(object);
                                iterator.remove();
                            }
                            canceledObjects.add(object);
                        }
                    }
                }
                if (canceledObjects.isEmpty()) {
                    // Inform the dispatchEvent method that it maybe can queue new listeners now
                    synchronized (queuedListenerTasks) {
                        queuedListenerTasks.notifyAll();
                    }
                }
                canceledObjects.forEach(this::checkRunningListenersAndStartIfPossible);
            } catch (Throwable t) {
                logger.error("Failed to check execution times!", t);
            }
        }, 200, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * Sets whether execution time checking should be enabled or not.
     *
     * @param enable Whether execution time checking should be enabled or not.
     */
    public void setExecutionTimeCheckingEnabled(boolean enable) {
        executionTimeCheckingEnabled = enable;
    }

    /**
     * Dispatches an event to the given listeners using the provided consumer.
     * Calling this method usually looks like this:
     * {@code dispatchEvent(server, listeners, listener -> listener.onXyz(event));}
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
        api.getThreadPool().getSingleThreadExecutorService("Event Dispatch Adder").submit(() -> { // (horrible name)
            if (object != null) { // Object dependent listeners
                // Don't allow adding of more events while there are unfinished object independent tasks
                synchronized (queuedListenerTasks) {
                    while (!queuedListenerTasks.get(OBJECT_INDEPENDENT_TASK_INDICATOR).isEmpty()) {
                        try {
                            // Just to be on the safe side, we use a timeout of 5 seconds
                            queuedListenerTasks.wait(5000);
                        } catch (InterruptedException ignored) { }
                    }
                }
                synchronized (queuedListenerTasks) {
                    Queue<Runnable> queue = queuedListenerTasks.computeIfAbsent(object, o -> new LinkedList<>());
                    listeners.forEach(listener -> queue.add(() -> consumer.accept(listener)));
                }
                checkRunningListenersAndStartIfPossible(object);
            } else { // Object independent listeners
                synchronized (queuedListenerTasks) {
                    Queue<Runnable> queue = queuedListenerTasks
                            .computeIfAbsent(OBJECT_INDEPENDENT_TASK_INDICATOR, o -> new LinkedList<>());
                    listeners.forEach(listener -> queue.add(() -> consumer.accept(listener)));
                }
                checkRunningListenersAndStartIfPossible(null);
            }
        });
    }

    /**
     * Checks if there are listeners running for the given object and if
     * not it takes one from the queue for the object and executes it.
     *
     * @param object The object which is used to determine in which thread the consumer should be executed.
     */
    private void checkRunningListenersAndStartIfPossible(Object object) {
        // Make sure to not accidentally run it if there are other non independent task waiting for execution
        if (object == OBJECT_INDEPENDENT_TASK_INDICATOR) {
            object = null;
        }
        synchronized (queuedListenerTasks) {
            Queue<Runnable> queue = object == null ? null : queuedListenerTasks.get(object);
            if (queue == null || queue.isEmpty()) {
                if (queuedListenerTasks.get(OBJECT_INDEPENDENT_TASK_INDICATOR).isEmpty()) {
                    return;
                }
                boolean moreTasks = queuedListenerTasks.entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .anyMatch(entry -> entry.getKey() != OBJECT_INDEPENDENT_TASK_INDICATOR);
                synchronized (runningListeners) {
                    if (moreTasks || !runningListeners.isEmpty()) {
                        return;
                    }
                }
                object = OBJECT_INDEPENDENT_TASK_INDICATOR;
                queue = queuedListenerTasks.get(OBJECT_INDEPENDENT_TASK_INDICATOR);
            }
            Object taskIndicator = object;
            final Queue<Runnable> finalQueue = queue;
            synchronized (runningListeners) {
                if (!runningListeners.contains(taskIndicator) && !queue.isEmpty()) {
                    runningListeners.add(taskIndicator);
                    // Yes, an array is a hacky solution, but whatever works ;-)
                    // Because of the synchronization on activeListener we can be sure, that activeListener[0] gets
                    // initialized before being accessed inside the runnable.
                    Future<?>[] activeListener = new Future<?>[1];
                    // Call the listener
                    synchronized (activeListener) {
                        activeListener[0] = api.getThreadPool().getExecutorService().submit(() -> {
                            if (taskIndicator instanceof ServerImpl) {
                                Object serverReadyNotifier = new Object();
                                ((ServerImpl) taskIndicator)
                                        .addServerReadyConsumer(s -> {
                                            synchronized (serverReadyNotifier) {
                                                serverReadyNotifier.notifyAll();
                                            }
                                        });
                                synchronized (serverReadyNotifier) {
                                    while (!((ServerImpl) taskIndicator).isReady()) {
                                        try {
                                            serverReadyNotifier.wait(5000);
                                        } catch (InterruptedException ignored) { }
                                    }
                                }
                            }
                            synchronized (activeListeners) {
                                synchronized (activeListener) {
                                    // Add the future to the list of active listeners
                                    activeListeners
                                            .put(activeListener[0], new Object[]{System.nanoTime(), taskIndicator});
                                }
                            }
                            try {
                                finalQueue.poll().run();
                            } catch (Throwable t) {
                                logger.error("Unhandled exception in {}!", () -> getThreadType(taskIndicator), () -> t);
                            }
                            synchronized (activeListeners) {
                                synchronized (runningListeners) {
                                    if (!Thread.interrupted()) {
                                        activeListeners.remove(activeListener[0]);
                                        runningListeners.remove(taskIndicator);
                                    }
                                }
                            }
                            // Inform the dispatchEvent method that it maybe can queue new listeners now
                            synchronized (queuedListenerTasks) {
                                queuedListenerTasks.notifyAll();
                            }
                            checkRunningListenersAndStartIfPossible(taskIndicator);
                        });
                    }

                }
            }
        }
    }

    /**
     * Gets the thread type used in log message for the given task indicator.
     *
     * @param taskIndicator The task indicator.
     * @return The name of the thread type.
     */
    private String getThreadType(Object taskIndicator) {
        String threadType;
        if (taskIndicator instanceof DiscordApi) {
            threadType = "global listener thread";
        } else if (taskIndicator == OBJECT_INDEPENDENT_TASK_INDICATOR) {
            threadType = "connection listener thread";
        } else {
            threadType = String.format("listener thread for %s", taskIndicator);
        }
        return threadType;
    }

}
