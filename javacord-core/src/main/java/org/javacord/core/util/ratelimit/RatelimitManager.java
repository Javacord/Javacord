package org.javacord.core.util.ratelimit;

import okhttp3.Response;
import org.apache.logging.log4j.Logger;
import org.javacord.api.exception.DiscordException;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestRequest;
import org.javacord.core.util.rest.RestRequestResponseInformationImpl;
import org.javacord.core.util.rest.RestRequestResult;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * This class manages ratelimits and keeps track of them.
 */
public class RatelimitManager {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(RatelimitManager.class);

    /**
     * The discord api instance for this ratelimit manager.
     */
    private final DiscordApiImpl api;

    /**
     * A set with all buckets.
     */
    private final Set<RatelimitBucket> buckets = new HashSet<>();

    /**
     * Creates a new ratelimit manager.
     *
     * @param api The discord api instance for this ratelimit manager.
     */
    public RatelimitManager(DiscordApiImpl api) {
        this.api = api;
    }

    /**
     * Gets a set with all ratelimit buckets.
     *
     * @return A set with all ratelimit buckets.
     */
    public Set<RatelimitBucket> getBuckets() {
        return buckets;
    }

    /**
     * Queues the given request.
     * This method is automatically called when using {@link RestRequest#execute(Function)}!
     *
     * @param request The request to queue.
     */
    public void queueRequest(RestRequest<?> request) {
        final RatelimitBucket bucket;
        final boolean alreadyInQueue;
        synchronized (buckets) {
            // Search for a bucket that fits to this request
            bucket = buckets.stream()
                    .filter(b -> b.equals(request.getEndpoint(), request.getMajorUrlParameter().orElse(null)))
                    .findAny()
                    .orElseGet(() -> new RatelimitBucket(
                            api, request.getEndpoint(), request.getMajorUrlParameter().orElse(null)));

            // Must be executed BEFORE adding the request to the queue
            alreadyInQueue = bucket.peekRequestFromQueue() != null;

            // Add the bucket to the set of buckets (does nothing if it's already in the set)
            buckets.add(bucket);

            // Add the request to the bucket's queue
            bucket.addRequestToQueue(request);
        }

        // If the bucket is already in the queue, there's nothing more to do
        if (alreadyInQueue) {
            return;
        }

        // Start working of the queue
        api.getThreadPool().getExecutorService().submit(() -> {
            RestRequest<?> currentRequest = bucket.peekRequestFromQueue();
            RestRequestResult result = null;
            long responseTimestamp = System.currentTimeMillis();
            while (currentRequest != null) {
                try {
                    int sleepTime = bucket.getTimeTillSpaceGetsAvailable();
                    if (sleepTime > 0) {
                        logger.debug("Delaying requests to {} for {}ms to prevent hitting ratelimits",
                                bucket, sleepTime);
                    }

                    // Sleep until space is available
                    while (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            logger.warn("We got interrupted while waiting for a rate limit!", e);
                        }
                        // Update in case something changed (e.g. because we hit a global ratelimit)
                        sleepTime = bucket.getTimeTillSpaceGetsAvailable();
                    }

                    // Execute the request
                    result = currentRequest.executeBlocking();

                    // Calculate the time offset, if it wasn't done before
                    responseTimestamp = System.currentTimeMillis();
                } catch (Throwable t) {
                    responseTimestamp = System.currentTimeMillis();
                    if (currentRequest.getResult().isDone()) {
                        logger.warn("Received exception for a request that is already done. "
                                + "This should not be able to happen!", t);
                    }
                    // Try to get the response from the exception if it exists
                    if (t instanceof DiscordException) {
                        result = ((DiscordException) t).getResponse()
                                .map(RestRequestResponseInformationImpl.class::cast)
                                .map(RestRequestResponseInformationImpl::getRestRequestResult)
                                .orElse(null);
                    }
                    // Complete the request
                    currentRequest.getResult().completeExceptionally(t);
                } finally {
                    try {
                        // Calculate offset
                        calculateOffset(responseTimestamp, result);
                        // Handle the response
                        handleResponse(currentRequest, result, bucket, responseTimestamp);
                    } catch (Throwable t) {
                        logger.warn("Encountered unexpected exception.", t);
                    }

                    // The request didn't finish, so let's try again
                    if (!currentRequest.getResult().isDone()) {
                        continue;
                    }

                    // Poll a new quest
                    synchronized (buckets) {
                        bucket.pollRequestFromQueue();
                        currentRequest = bucket.peekRequestFromQueue();
                        if (currentRequest == null) {
                            buckets.remove(bucket);
                        }
                    }
                }
            }
        });
    }

    /**
     * Updates the ratelimit information and sets the result if the request was successful.
     *
     * @param request The request.
     * @param result The result of the request.
     * @param bucket The bucket the request belongs to.
     * @param responseTimestamp The timestamp directly after the response finished.
     */
    private void handleResponse(
            RestRequest<?> request, RestRequestResult result, RatelimitBucket bucket, long responseTimestamp) {
        if (result == null || result.getResponse() == null) {
            return;
        }
        Response response = result.getResponse();
        boolean global = response.header("X-RateLimit-Global", "false").equalsIgnoreCase("true");
        int remaining = Integer.parseInt(response.header("X-RateLimit-Remaining", "1"));
        long reset = request
                .getEndpoint()
                .getHardcodedRatelimit()
                .map(ratelimit -> responseTimestamp + api.getTimeOffset() + ratelimit)
                .orElseGet(() -> (long) (Double.parseDouble(response.header("X-RateLimit-Reset", "0")) * 1000));

        // Check if we received a 429 response
        if (result.getResponse().code() == 429) {
            int retryAfter =
                    result.getJsonBody().isNull() ? 0 : result.getJsonBody().get("retry_after").asInt();

            if (global) {
                // We hit a global ratelimit. Time to panic!
                logger.warn("Hit a global ratelimit! This means you were sending a very large "
                        + "amount within a very short time frame.");
                RatelimitBucket.setGlobalRatelimitResetTimestamp(api, responseTimestamp + retryAfter);
            } else {
                logger.debug("Received a 429 response from Discord! Recalculating time offset...");
                // Setting the offset to null causes a recalculate for the next request
                api.setTimeOffset(null);

                // Update the bucket information
                bucket.setRatelimitRemaining(0);
                bucket.setRatelimitResetTimestamp(responseTimestamp + retryAfter);
            }
        } else {
            // Check if we didn't already complete it exceptionally.
            CompletableFuture<RestRequestResult> requestResult = request.getResult();
            if (!requestResult.isDone()) {
                requestResult.complete(result);
            }

            // Update bucket information
            bucket.setRatelimitRemaining(remaining);
            bucket.setRatelimitResetTimestamp(reset);
        }
    }

    /**
     * Calculates the offset of the local time and discord's time.
     *
     * @param currentTime The current time.
     * @param result The result of the rest request.
     */
    private void calculateOffset(long currentTime, RestRequestResult result) {
        // Double-checked locking for better performance
        if ((api.getTimeOffset() != null) || (result == null) || (result.getResponse() == null)) {
            return;
        }
        synchronized (api) {
            if (api.getTimeOffset() == null) {
                // Discord sends the date in their header in the format RFC_1123_DATE_TIME
                // We use this header to calculate a possible offset between our local time and the discord time
                String date = result.getResponse().header("Date");
                if (date != null) {
                    long discordTimestamp = OffsetDateTime.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME)
                            .toInstant().toEpochMilli();
                    api.setTimeOffset((discordTimestamp - currentTime));
                    logger.debug("Calculated an offset of {} to the Discord time.", api::getTimeOffset);
                }
            }
        }
    }

}
