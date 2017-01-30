/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.utils;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

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
    private ListeningExecutorService listeningExecutorService = null;
    private final ConcurrentHashMap<String, ExecutorService> executorServiceSingeThreads = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of this class.
     */
    public ThreadPool() {
        executorService = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new SynchronousQueue<Runnable>());
        listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
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

    /**
     * Gets the used listening executor service instance.
     *
     * @return The used listening executor service instance.
     */
    public ListeningExecutorService getListeningExecutorService() {
        return listeningExecutorService;
    }

}
