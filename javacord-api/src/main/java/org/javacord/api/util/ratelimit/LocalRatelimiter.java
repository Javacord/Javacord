package org.javacord.api.util.ratelimit;

/**
 * An implementation of {@code Ratelimiter} that allows simple local ratelimits.
 */
public class LocalRatelimiter implements Ratelimiter {

    private volatile long nextResetNanos;
    private volatile int remainingQuota;

    private final int amount;
    private final int seconds;

    /**
     * Creates a new local ratelimiter.
     *
     * @param amount The amount available per reset interval.
     * @param seconds The time to wait until the available quota resets.
     */
    public LocalRatelimiter(int amount, int seconds) {
        this.amount = amount;
        this.seconds = seconds;
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
     * Gets the time to wait until the available quota resets in seconds.
     *
     * @return The time to wait until the available quota resets.
     */
    public int getSeconds() {
        return seconds;
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
                Thread.sleep(sleepTime);
            }
        }

        // Reset the limit when the last reset timestamp is past
        if (System.nanoTime() > nextResetNanos) {
            remainingQuota = amount;
            nextResetNanos = System.nanoTime() + seconds * 1_000_000_000L;
        }

        remainingQuota--;
    }

    private long calculateSleepTime() {
        return (nextResetNanos - System.nanoTime()) / 1_000_000;
    }
}
