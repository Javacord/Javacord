package org.javacord.core.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An object reference that has the ability to block a thread until a value is available if one is not present.
 *
 * @param <V> The type of object referred to by this reference.
 */
public class BlockingReference<V> {

    private final Lock lock = new ReentrantLock();

    private final Condition hasValue = lock.newCondition();

    private volatile V value;

    /**
     * Creates a new BlockingReference instance with the given initial value.
     *
     * @param initialValue The initial value.
     */
    public BlockingReference(V initialValue) {
        this.value = initialValue;
    }

    /**
     * Creates a new BlockingReference instance with no initial value.
     */
    public BlockingReference() {
    }

    /**
     * Checks whether this reference currently holds a value.
     *
     * @return Whether or not this reference currently holds a value.
     */
    public boolean hasValue() {
        return value != null;
    }

    /**
     * Gets the current value or blocks the thread until one is present.
     *
     * @return The current value.
     * @throws InterruptedException if interrupted while waiting.
     */
    public V get() throws InterruptedException {
        lock.lock();
        try {
            while (value == null) {
                hasValue.await();
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the current value or blocks the thread until one is present.
     *
     * @param timeout The amount of time to wait in the given time unit before unblocking the thread and returning null.
     * @param unit    The TimeUnit to use to interpret timeout
     * @return The current value or null if the specified timeout elapses.
     * @throws InterruptedException if interrupted while waiting.
     */
    public V get(long timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            if (value == null) {
                hasValue.await(timeout, unit);
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the current value and notifies any waiting {@link #get()} calls.
     *
     * @param value The value to set.
     */
    public void set(V value) {
        lock.lock();
        try {
            this.value = value;
            if (value != null) {
                hasValue.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
