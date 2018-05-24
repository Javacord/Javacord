package org.javacord.core.util.cache;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.logging.LoggerUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The implementation of {@link MessageCache}.
 */
public class MessageCacheImpl implements MessageCache, Cleanupable {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageCacheImpl.class);

    /**
     * A list with all messages.
     */
    private final List<Message> messages = new ArrayList<>();

    /**
     * The cache clean future to be cancelled in {@link #cleanup()}.
     */
    private final Future<?> cleanFuture;

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The maximum amount of stored messages.
     */
    private volatile int capacity;

    /**
     * The time how long messages should be cached.
     */
    private volatile int storageTimeInSeconds;

    /**
     * Creates a new message cache.
     *
     * @param api The discord api instance.
     * @param capacity The capacity of the cache, not including messages which are cached forever.
     * @param storageTimeInSeconds The storage time in seconds.
     */
    public MessageCacheImpl(DiscordApi api, int capacity, int storageTimeInSeconds) {
        this.api = (DiscordApiImpl) api;
        this.capacity = capacity;
        this.storageTimeInSeconds = storageTimeInSeconds;

        cleanFuture = api.getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
            try {
                clean();
            } catch (Throwable t) {
                logger.error("Failed to clean message cache!", t);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Adds a message to the cache.
     *
     * @param message The message to add.
     */
    public void addMessage(Message message) {
        synchronized (messages) {
            api.addMessageToCache(message);
            if (messages.contains(message)) {
                return;
            }
            // Add the message in the correct order
            int pos = Collections.binarySearch(messages, message);
            if (pos < 0) {
                pos = -pos - 1;
            }
            messages.add(pos, message);
        }
    }

    /**
     * Removes a message from the cache.
     *
     * @param message The message to remove.
     */
    public void removeMessage(Message message) {
        synchronized (messages) {
            messages.remove(message);
        }
    }

    /**
     * Cleans the cache.
     */
    public void clean() {
        Instant minAge = Instant.ofEpochMilli(System.currentTimeMillis() - storageTimeInSeconds * 1000);
        synchronized (messages) {
            messages.removeIf(message -> !message.isCachedForever() && message.getCreationTimestamp().isBefore(minAge));
            long foreverCachedAmount = messages.stream().filter(Message::isCachedForever).count();
            messages.removeAll(messages.stream()
                                       .filter(message -> !message.isCachedForever())
                                       .limit(Math.max(0, messages.size() - capacity - foreverCachedAmount))
                                       .collect(Collectors.toList()));
        }
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity >= 0 ? capacity : 0;
    }

    @Override
    public int getStorageTimeInSeconds() {
        return storageTimeInSeconds;
    }

    @Override
    public void setStorageTimeInSeconds(int storageTimeInSeconds) {
        this.storageTimeInSeconds = storageTimeInSeconds >= 0 ? storageTimeInSeconds : 0;
    }

    @Override
    public void cleanup() {
        cleanFuture.cancel(false);
    }
}
