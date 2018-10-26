package org.javacord.core.util.concurrent;

import org.javacord.api.util.concurrent.ThreadPool;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The implementation of {@link ThreadPool}.
 */
public class ThreadPoolImpl implements ThreadPool {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final ExecutorService executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new SynchronousQueue<>(),
            new ThreadFactory("Javacord - Central ExecutorService - %d", false));
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            CORE_POOL_SIZE, new ThreadFactory("Javacord - Central Scheduler - %d", false));
    private final ScheduledExecutorService daemonScheduler = Executors.newScheduledThreadPool(
            CORE_POOL_SIZE, new ThreadFactory("Javacord - Central Daemon Scheduler - %d", true));
    private final ConcurrentHashMap<String, ExecutorService> executorServiceSingleThreads = new ConcurrentHashMap<>();

    /**
     * Shutdowns the thread pool.
     * This method is called automatically after disconnecting.
     */
    public void shutdown() {
        executorService.shutdown();
        scheduler.shutdown();
        daemonScheduler.shutdown();
        executorServiceSingleThreads.values().forEach(ExecutorService::shutdown);
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    @Override
    public ScheduledExecutorService getDaemonScheduler() {
        return daemonScheduler;
    }

    @Override
    public ExecutorService getSingleThreadExecutorService(String threadName) {
        return executorServiceSingleThreads.computeIfAbsent(threadName, key ->
                new ThreadPoolExecutor(0, 1, KEEP_ALIVE_TIME, TIME_UNIT, new LinkedBlockingQueue<>(),
                                       new ThreadFactory("Javacord - " + threadName, false)));
    }

    @Override
    public ExecutorService getSingleDaemonThreadExecutorService(String threadName) {
        return executorServiceSingleThreads.computeIfAbsent(threadName, key ->
                new ThreadPoolExecutor(0, 1, KEEP_ALIVE_TIME, TIME_UNIT, new LinkedBlockingQueue<>(),
                                       new ThreadFactory("Javacord - " + threadName, true)));
    }

    @Override
    public Optional<ExecutorService> removeAndShutdownSingleThreadExecutorService(String threadName) {
        ExecutorService executorService = executorServiceSingleThreads.remove(threadName);
        if (executorService != null) {
            executorService.shutdown();
        }
        return Optional.ofNullable(executorService);
    }
}
