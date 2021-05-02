package org.javacord.api.util.ratelimit;

import org.javacord.api.DiscordApiBuilder;

import java.time.Duration;

/**
 * An implementation of {@code Ratelimiter} that allows for simple local ratelimits.
 *
 * <p>To compensate for misalignment with the real global ratelimit bucket, it is recommended to not set the value
 * to the exact global ratelimit but a lower value. If the global ratelimit for your bot is 50 requests / 1 second
 * (the default), you should choose a value that prevents overlapping with the real bucket. This can be achieved with
 * the following rules:
 * <ul>
 * <li>Choose an {@code amount} that is between {@code 1} and half of the real amount
 *     (in this example {@code 1} - {@code 25}).
 * <li>Calculate the {@code bucketDuration} using {@code (amount * realDuration) / (realAmount - amount)}.
 * </ul>
 * For the 50 requests / 1 second ratelimit, these rules allow the following ratelimits (only a subset):
 * <ul>
 * <li>About {@code 1} request per {@code 21 ms}
 *     ({@code amount = 1} and {@code bucketDuration = Duration.ofMillis((long) Math.ceil(1000D / 49D))})
 * <li>About {@code 5} request per {@code 112 ms}
 *     ({@code amount = 5} and {@code bucketDuration = Duration.ofMillis((long) Math.ceil(5000D / 45D))}).
 * <li>{@code 10} request per {@code 250 ms}
 *     ({@code amount = 10} and {@code bucketDuration = Duration.ofMillis((long) Math.ceil(10000D / 40D))}).
 * <li>{@code 25} request per {@code 1 sec}
 *     ({@code amount = 25} and {@code bucketDuration = Duration.ofMillis((long) Math.ceil(25000D / 25D))}).
 * </ul>
 * Choosing a lower {@code amount} increases the maximum throughput but can limits your ability to perform actions
 * in bulk.
 *
 * @see <a href="https://javacord.org/wiki/advanced-topics/ratelimits.html">Related wiki article</a>
 * @see DiscordApiBuilder#setGlobalRatelimiter(Ratelimiter)
 */
public class LocalRatelimiter implements Ratelimiter {

    private volatile long nextResetNanos;
    private volatile int remainingQuota;

    private final int amount;
    private final Duration bucketDuration;

    /**
     * Creates a new local ratelimiter.
     *
     * @param amount The amount available per reset interval.
     * @param seconds The time to wait until the available quota resets.
     * @deprecated Use {@link #LocalRatelimiter(int, Duration)} instead.
     */
    @Deprecated
    public LocalRatelimiter(int amount, int seconds) {
        this.amount = amount;
        bucketDuration = Duration.ofSeconds(seconds);
    }

    /**
     * Creates a new local ratelimiter.
     *
     * @param amount The amount available per reset interval.
     * @param bucketDuration The time to wait until the available quota resets.
     */
    public LocalRatelimiter(int amount, Duration bucketDuration) {
        this.amount = amount;
        this.bucketDuration = bucketDuration;
    }

    /**
     * Gets the amount available per reset interval.
     *
     * @return The amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the time to wait until the available quota resets.
     *
     * @return The time to wait until the available quota resets.
     */
    public Duration getBucketDuration() {
        return bucketDuration;
    }

    /**
     * Gets the time to wait until the available quota resets in seconds.
     *
     * @return The time to wait until the available quota resets.
     * @deprecated Use {@link #getBucketDuration()} instead.
     */
    @Deprecated
    public int getSeconds() {
        return (int) bucketDuration.getSeconds();
    }

    /**
     * Gets the next time the quota resets.
     *
     * <p>Use {@link System#nanoTime()} to calculate the absolute difference.
     *
     * @return The next time the quota resets. Can be in the past.
     */
    public long getNextResetNanos() {
        return nextResetNanos;
    }

    /**
     * Gets the remaining quota in the current reset interval.
     *
     * @return The remaining quota.
     */
    public int getRemainingQuota() {
        return remainingQuota;
    }

    @Override
    public synchronized void requestQuota() throws InterruptedException {
        if (remainingQuota <= 0) {
            // Wait until a new quota becomes available
            long sleepTime;
            while ((sleepTime = calculateSleepTime()) > 0) { // Sleep is unreliable, so we have to loop
                Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
            }
        }

        // Reset the limit when the last reset timestamp is past
        if (System.nanoTime() >= nextResetNanos) {
            remainingQuota = amount;
            try {
                nextResetNanos = System.nanoTime() + bucketDuration.toNanos();
            } catch (ArithmeticException e) {
                // An ArithmeticException means that the duration was too large to be represented
                // as a long. While such a value is completely non-sense and should not be used, we
                // still don't want an exception.
                nextResetNanos = Long.MAX_VALUE;
            }
        }

        remainingQuota--;
    }

    private long calculateSleepTime() {
        return nextResetNanos - System.nanoTime();
    }
}
