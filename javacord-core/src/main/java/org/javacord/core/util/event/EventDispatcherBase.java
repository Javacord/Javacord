package org.javacord.core.util.event;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * This class is the base for the class used to dispatch events.
 */
public abstract class EventDispatcherBase {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EventDispatcherBase.class);

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
     * Whether execution time checking should be enabled or not.
     */
    private volatile boolean executionTimeCheckingEnabled = true;

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * This map which holds a queue for every object (usually a server) with tasks to call the waiting listeners.
     */
    private final Map<DispatchQueueSelector, Queue<Runnable>> queuedListenerTasks =
            Collections.synchronizedMap(new HashMap<>());

    /**
     * A list with all objects which currently have a running listener.
     */
    private final Set<DispatchQueueSelector> runningListeners = Collections.synchronizedSet(new HashSet<>());

    /**
     * A map with all running listeners as it's key. The value contains an array where the first element is a long
     * with the start time of the listener (using {@link System#nanoTime()} and the second element is the object
     * of the listener (usually a server).
     */
    private final Map<AtomicReference<Future<?>>, Object[]> activeListeners =
            Collections.synchronizedMap(new HashMap<>());

    /**
     * Creates a new event dispatcher.
     *
     * @param api The discord api instance.
     */
    protected EventDispatcherBase(DiscordApiImpl api) {
        this.api = api;
        queuedListenerTasks.put(null, new ConcurrentLinkedQueue<>());
        api.getThreadPool().getScheduler().scheduleAtFixedRate(() -> {
            try {
                if (!executionTimeCheckingEnabled) {
                    return;
                }
                List<DispatchQueueSelector> canceledObjects = new ArrayList<>();
                synchronized (activeListeners) {
                    long currentNanoTime = System.nanoTime();
                    Iterator<Map.Entry<AtomicReference<Future<?>>, Object[]>> iterator =
                            activeListeners.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<AtomicReference<Future<?>>, Object[]> entry = iterator.next();
                        long difference = currentNanoTime - ((long) entry.getValue()[0]);
                        DispatchQueueSelector queueSelector = (DispatchQueueSelector) entry.getValue()[1];
                        if (difference > DEBUG_WARNING_DELAY_IN_MILLIS * 1_000_000L
                                && difference < DEBUG_WARNING_DELAY_IN_MILLIS * 1_000_000L + 201_000_000L) {
                            logger.debug("Detected a {} which is now running for over {}ms ({}ms). This is"
                                            + " an unusually long execution time for a listener task. Make"
                                            + " sure to not do any heavy computations in listener threads!",
                                    () -> getThreadType(queueSelector),
                                    () -> DEBUG_WARNING_DELAY_IN_MILLIS,
                                    () -> (int) (difference / 1_000_000L));
                        }
                        if (difference > INFO_WARNING_DELAY_IN_SECONDS * 1_000_000_000L
                                && difference < INFO_WARNING_DELAY_IN_SECONDS * 1_000_000_000L + 201_000_000L) {
                            logger.warn("Detected a {} which is now running for over {} seconds ({}ms)."
                                            + " This is a very unusually long execution time for a listener task. Make"
                                            + " sure to not do any heavy computations in listener threads!",
                                    () -> getThreadType(queueSelector),
                                    () -> INFO_WARNING_DELAY_IN_SECONDS,
                                    () -> (int) (difference / 1_000_000L));
                        }
                        if (difference > MAX_EXECUTION_TIME_IN_SECONDS * 1_000_000_000L) {
                            entry.getKey().get().cancel(true);
                            logger.error("Interrupted a {}, because it was running over {} seconds! This was most "
                                            + "likely caused by a deadlock or very heavy computation/blocking "
                                            + "operations in the listener thread. "
                                            + "Make sure to not block listener threads!",
                                    () -> getThreadType(queueSelector), () -> MAX_EXECUTION_TIME_IN_SECONDS);
                            runningListeners.remove(queueSelector);
                            iterator.remove();
                            canceledObjects.add(queueSelector);
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
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    protected DiscordApiImpl getApi() {
        return api;
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
     * @param queueSelector The object which is used to determine in which queue the event should be dispatched. Usually
     *                      the object is a server object (for server-dependent events), a discord api instance (for
     *                      server-independent events, like DMs or GMs) or {@code null} (for lifecycle events, like
     *                      connection lost, resume or reconnect). Providing {@code null} means, that already started
     *                      dispatchings are going to be finished, then all events with a {@code null} queue selector
     *                      are dispatched and finally other events are dispatched like normal again. Events with the
     *                      same queue selector are dispatched sequentially in the correct order of enqueueal, but on
     *                      arbitrary threads from a thread pool. Events with different queue selectors are dispatched
     *                      in parallel.
     * @param listeners     A list with listeners which get consumed by the given consumer.
     * @param consumer      A consumer which consumes all listeners from the given list and is meant to call their
     *                      {@code onXyz(Event)} method.
     * @param <T>           The type of the listener.
     */
    protected <T> void dispatchEvent(DispatchQueueSelector queueSelector, List<T> listeners, Consumer<T> consumer) {
        api.getThreadPool().getSingleThreadExecutorService("Event Dispatch Queues Manager").submit(() -> {
            if (queueSelector != null) { // Object dependent listeners
                // Don't allow adding of more events while there are unfinished object independent tasks
                Queue<Runnable> objectIndependentQueue = queuedListenerTasks.get(null);
                while (!objectIndependentQueue.isEmpty()) {
                    try {
                        synchronized (queuedListenerTasks) {
                            // Just to be on the safe side, we use a timeout of 5 seconds
                            queuedListenerTasks.wait(5000);
                        }
                    } catch (InterruptedException ignored) { }
                }
            }
            Queue<Runnable> queue = queuedListenerTasks.computeIfAbsent(
                    queueSelector, o -> new ConcurrentLinkedQueue<>());
            listeners.forEach(listener -> queue.add(() -> consumer.accept(listener)));
            checkRunningListenersAndStartIfPossible(queueSelector);
        });
    }

    /**
     * Checks if there are listeners running for the given object and if
     * not it takes one from the queue for the object and executes it.
     *
     * <p>If all object-dependent tasks are finished,
     * object-independent ones will be executed instead
     * and after all object-independent are finished,
     * new object-dependent tasks can get queued.
     *
     * @param queueSelector The object which is used to determine in which thread the consumer should be executed.
     */
    private void checkRunningListenersAndStartIfPossible(DispatchQueueSelector queueSelector) {
        synchronized (queuedListenerTasks) {
            // if either
            // - running for object-independent tasks or
            // - running for object-dependent tasks, but queue is empty or not present
            Queue<Runnable> queue = queueSelector == null ? null : queuedListenerTasks.get(queueSelector);
            if (queue == null || queue.isEmpty()) {
                // if no object-independent tasks to be processed everything is fine, return
                if (queuedListenerTasks.get(null).isEmpty()) {
                    return;
                }
                // if object-independent tasks to be processed
                // check whether there are still queued object-dependent tasks
                boolean moreObjectDependentTasks = queuedListenerTasks.entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .anyMatch(entry -> entry.getKey() != null);
                // if there are object-dependent tasks scheduled or there are still tasks running => retry later
                if (moreObjectDependentTasks || !runningListeners.isEmpty()) {
                    return;
                }
                // force object-independent task being handled
                // after all queued object-dependent tasks are finished
                // and before new object-dependent tasks will get queued
                queueSelector = null;
                queue = queuedListenerTasks.get(null);
            }
            DispatchQueueSelector finalQueueSelector = queueSelector;
            Queue<Runnable> taskQueue = queue;
            // if there is something to execute and there is task running already
            if (!queue.isEmpty() && runningListeners.add(finalQueueSelector)) {
                AtomicReference<Future<?>> activeListener = new AtomicReference<>();
                activeListener.set(api.getThreadPool().getExecutorService().submit(() -> {
                    if (finalQueueSelector instanceof ServerImpl) {
                        Object serverReadyNotifier = new Object();
                        ((ServerImpl) finalQueueSelector)
                                .addServerReadyConsumer(s -> {
                                    synchronized (serverReadyNotifier) {
                                        serverReadyNotifier.notifyAll();
                                    }
                                });
                        while (!((ServerImpl) finalQueueSelector).isReady()) {
                            try {
                                synchronized (serverReadyNotifier) {
                                    serverReadyNotifier.wait(5000);
                                }
                            } catch (InterruptedException ignored) { }
                        }
                    }
                    // Add the future to the list of active listeners
                    activeListeners.put(activeListener, new Object[]{System.nanoTime(), finalQueueSelector});
                    try {
                        taskQueue.poll().run();
                    } catch (Throwable t) {
                        logger.error(() -> "Unhandled exception in " + getThreadType(finalQueueSelector) + "!", t);
                    }
                    activeListeners.remove(activeListener);
                    runningListeners.remove(finalQueueSelector);
                    // Inform the dispatchEvent method that it maybe can queue new listeners now
                    synchronized (queuedListenerTasks) {
                        queuedListenerTasks.notifyAll();
                    }
                    checkRunningListenersAndStartIfPossible(finalQueueSelector);
                }));
            }
        }
    }

    /**
     * Gets the thread type used in log message for the given queue selector.
     *
     * @param queueSelector The queue selector.
     * @return The name of the thread type.
     */
    private String getThreadType(DispatchQueueSelector queueSelector) {
        String threadType;
        if (queueSelector instanceof DiscordApi) {
            threadType = "a global listener thread";
        } else if (queueSelector == null) {
            threadType = "a connection listener thread";
        } else {
            threadType = String.format("a listener thread for %s", queueSelector);
        }
        return threadType;
    }

}
