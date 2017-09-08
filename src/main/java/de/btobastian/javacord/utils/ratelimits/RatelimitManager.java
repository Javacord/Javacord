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
package de.btobastian.javacord.utils.ratelimits;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import de.btobastian.javacord.utils.exceptions.RatelimitException;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.*;

/**
 * This class manages ratelimits and keeps track of them.
 */
public class RatelimitManager {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(RatelimitManager.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Set<RatelimitBucket> buckets = ConcurrentHashMap.newKeySet();
    private final HashMap<RatelimitBucket, ConcurrentLinkedQueue<RestRequest>> queues = new HashMap<>();

    /**
     * Adds a request to the queue based on the ratelimit bucket.
     * This method is automatically called when using {@link RestRequest#execute()}!
     *
     * @param request The request to queue.
     */
    public void queueRequest(RestRequest request) {
        // Get the bucket for the current request type.
        RatelimitBucket bucket = buckets
                .parallelStream()
                .filter(b -> b.equals(request.getEndpoint(), request.getMajorUrlParameter().orElse(null)))
                .findAny()
                .orElse(new RatelimitBucket(request.getEndpoint(), request.getMajorUrlParameter().orElse(null)));

        // Get the queue for the current bucket or create a new one if there's no one already
        ConcurrentLinkedQueue<RestRequest> queue = queues.computeIfAbsent(bucket, k -> new ConcurrentLinkedQueue<>());

        // Add the request to the queue and check if there's already a scheduler working on the queue
        boolean startScheduler = false;
        synchronized (bucket) {
            synchronized (queue) {
                if (bucket.hasActiveScheduler()) {
                    queue.add(request);
                } else {
                    bucket.setHasActiveScheduler(true);
                    queue.add(request);
                    startScheduler = true;
                }
            }
        }

        if (!startScheduler) {
            return;
        }

        // Start a scheduler to work off the queue
        scheduler.schedule(() -> {
            while (!queue.isEmpty()) {
                if (!bucket.hasSpace()) {
                    synchronized (queue) {
                        // Remove if we retried to often
                        queue.removeIf(req -> {
                            if (req.incrementRetryCounter()) {
                                req.getResult().completeExceptionally(
                                        new RatelimitException("You have been ratelimited and ran out of retires!")
                                );
                                return true;
                            }
                            return false;
                        });
                        if (queue.isEmpty()) {
                            break;
                        }
                    }
                    try {
                        Thread.sleep(bucket.getTimeTillSpaceGetsAvailable() * 1000);
                    } catch (InterruptedException e) {
                        logger.warn("We got interrupted while waiting for a rate limit!", e);
                    }
                }
                RestRequest restRequest = queue.poll();
                try {
                    HttpResponse<JsonNode> response = restRequest.executeBlocking();
                    restRequest.getResult().complete(response);

                    String remaining = response.getHeaders().getFirst("X-RateLimit-Remaining");
                    String reset = response.getHeaders().getFirst("X-RateLimit-Reset");
                    String global = response.getHeaders().getFirst("X-RateLimit-Global");

                    if (global != null && global.equals("true")) {
                        // Mark the endpoint as global
                        bucket.getEndpoint().ifPresent(endpoint -> endpoint.setGlobal(true));
                    }

                    bucket.setRateLimitRemaining(Integer.parseInt(remaining));
                    bucket.setRateLimitResetTimestamp(Integer.parseInt(reset));
                } catch (Exception e) {
                    restRequest.getResult().completeExceptionally(e);
                }
                queue.peek();
            }
            synchronized (bucket) {
                bucket.setHasActiveScheduler(false);
            }
        }, bucket.getTimeTillSpaceGetsAvailable(), TimeUnit.SECONDS);
    }

}
