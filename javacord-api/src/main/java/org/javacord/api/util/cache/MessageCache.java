package org.javacord.api.util.cache;

/**
 * This class is used to cache message.
 */
public interface MessageCache {

    /**
     * Gets the capacity of the message cache.
     * Please notice that the cache is cleared only once every minute!
     *
     * @return The capacity of the message cache.
     */
    int getCapacity();

    /**
     * Sets the capacity of the message cache.
     * Messages which are cached forever are not included in this limit.
     * Please notice that the cache is cleared only once every minute!
     *
     * @param capacity The capacity of the message cache.
     */
    void setCapacity(int capacity);

    /**
     * Gets the maximum age of the message in seconds.
     * Please notice that the cache is cleared only once every minute!
     *
     * @return The maximum age of the message in seconds.
     */
    int getStorageTimeInSeconds();

    /**
     * Sets maximum age of old messages in seconds.
     * Please notice that the cache is cleared only once every minute!
     *
     * @param storageTimeInSeconds The maximum age in seconds.
     */
    void setStorageTimeInSeconds(int storageTimeInSeconds);

    /**
     * Sets whether automatic message cache cleanup is enabled.
     *
     * @param automaticCleanupEnabled Whether automatic message cache cleanup is enabled.
     */
    void setAutomaticCleanupEnabled(boolean automaticCleanupEnabled);

}
