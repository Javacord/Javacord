package org.javacord.api.util.concurrent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * This class creates and contains thread pools which are used by Javacord.
 */
public interface ThreadPool {

    /**
     * Gets the used executor service.
     *
     * @return The used executor service.
     */
    ExecutorService getExecutorService();

    /**
     * Gets the used scheduler.
     *
     * @return The used scheduler.
     */
    ScheduledExecutorService getScheduler();

    /**
     * Gets the used daemon scheduler.
     *
     * @return The used daemon scheduler.
     */
    ScheduledExecutorService getDaemonScheduler();

    /**
     * Gets an executor service which only uses a single thread.
     *
     * @param threadName The thread name of the executor service.
     *                   Will create a new one if the thread name is used the first time.
     * @return The executor service with the given thread name. Never {@code null}!
     */
    ExecutorService getSingleThreadExecutorService(String threadName);

    /**
     * Gets an executor service which only uses a single daemon thread.
     *
     * @param threadName The thread name of the executor service.
     *                   Will create a new one if the thread name is used the first time.
     * @return The executor service with the given thread name. Never {@code null}!
     */
    ExecutorService getSingleDaemonThreadExecutorService(String threadName);

    /**
     * Removes an existing executor service.
     *
     * <p>This allows you to get a fresh executor service when calling {@link #getSingleThreadExecutorService(String)}
     * again.
     *
     * @param threadName The thread name of the executor service.
     * @return The removed and shutdown executor service with the given thread name.
     */
    Optional<ExecutorService> removeAndShutdownSingleThreadExecutorService(String threadName);

    /**
     * Executes code after a given duration.
     *
     * <p>Tasks will be scheduled on the daemon executor, allowing the bot to shutdown without
     * all tasks being executed. This method is not meant to persist scheduled task over
     * multiple bot life cycles.</p>
     *
     * @param task The code to run.
     * @param duration The duration to run the code after.
     * @param unit The unit of the duration given.
     * @return A future that completes when the scheduled task is finished.
     * @param <T> The return type of the future.
     */
    <T> CompletableFuture<T> runAfter(Supplier<CompletableFuture<T>> task, long duration, TimeUnit unit);
}
