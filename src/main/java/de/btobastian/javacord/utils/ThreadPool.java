package de.btobastian.javacord.utils;

import java.util.concurrent.*;

/**
 * This class creates and contains thread pools which are used by this plugin.
 */
public class ThreadPool {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private ExecutorService executorService = null;
    private final ConcurrentHashMap<String, ExecutorService> executorServiceSingeThreads = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of this class.
     */
    public ThreadPool() {
        executorService = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new SynchronousQueue<>());
    }

    /**
     * Gets the used executor service instance.
     *
     * @return The used executor service instance.
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Gets an executor service which only uses a single thread.
     *
     * @param id The id of the executor service. Will create a new one if the id is used the first time.
     * @return The executor service with the given id. Never <code>null</code>!
     */
    public ExecutorService getSingleThreadExecutorService(String id) {
        synchronized (executorServiceSingeThreads) {
            ExecutorService service = executorServiceSingeThreads.get(id);
            if (service == null) {
                service = Executors.newSingleThreadExecutor();
                executorServiceSingeThreads.put(id, service);
            }
            return service;
        }
    }

}