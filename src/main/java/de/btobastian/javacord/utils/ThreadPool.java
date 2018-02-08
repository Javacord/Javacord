package de.btobastian.javacord.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class creates and contains thread pools which are used by this plugin.
 */
public class ThreadPool {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final ExecutorService executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new SynchronousQueue<>(),
            new ThreadFactory("Javacord - Central ExecutorService - %d", false));
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            CORE_POOL_SIZE, new ThreadFactory("Javacord - Central Scheduler - %d", false));
    private final ConcurrentHashMap<String, ExecutorService> executorServiceSingleThreads = new ConcurrentHashMap<>();

    /**
     * Gets the used executor service.
     *
     * @return The used executor service.
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Gets the used scheduler.
     *
     * @return The used scheduler.
     */
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * Shutdowns the thread pool.
     * This method is called automatically after disconnecting.
     */
    public void shutdown() {
        executorService.shutdown();
        scheduler.shutdown();
        executorServiceSingleThreads.values().forEach(ExecutorService::shutdown);
    }

    /**
     * Gets an executor service which only uses a single thread.
     *
     * @param id The id of the executor service. Will create a new one if the id is used the first time.
     * @return The executor service with the given id. Never <code>null</code>!
     */
    public ExecutorService getSingleThreadExecutorService(String id) {
        return executorServiceSingleThreads.computeIfAbsent(id, key ->
                Executors.newSingleThreadExecutor(new ThreadFactory("Javacord - '" + id + "' Processor", false)));
    }

}