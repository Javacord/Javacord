package org.javacord.core.util.cache;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.Message;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.logging.LoggerUtil;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
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
    private final List<Reference<? extends Message>> messages = new ArrayList<>();

    /**
     * The queue that is notified if a message became softly-reachable.
     */
    private final ReferenceQueue<Message> messagesCleanupQueue = new ReferenceQueue<>();

    /**
     * The messages cleanup future to be cancelled in {@link #cleanup()}.
     */
    private final Future<?> messagesCleanupFuture;

    /**
     * A list with all messages that should be cached forever.
     */
    private final List<Message> cacheForeverMessages = Collections.synchronizedList(new ArrayList<>());

    /**
     * The cache clean future to be cancelled in {@link #cleanup()}.
     */
    private final AtomicReference<Future<?>> cleanFuture = new AtomicReference<>();

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
     * @param api                     The discord api instance.
     * @param capacity                The capacity of the cache, not including messages which are cached forever.
     * @param storageTimeInSeconds    The storage time in seconds.
     * @param automaticCleanupEnabled Whether automatic message cache cleanup is enabled.
     */
    public MessageCacheImpl(DiscordApiImpl api, int capacity, int storageTimeInSeconds,
                            boolean automaticCleanupEnabled) {
        this.api = api;
        this.capacity = capacity;
        this.storageTimeInSeconds = storageTimeInSeconds;

        setAutomaticCleanupEnabled(automaticCleanupEnabled);

        if (api.isAddAllMessageToCacheEnabled()) {
            // After minimum JDK 9 is required this can be switched to use a Cleaner
            messagesCleanupFuture = api.getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                api.getMessageCacheLock().lock();
                try {
                    int removedMessages = 0;
                    for (Reference<? extends Message> messageRef = messagesCleanupQueue.poll();
                         messageRef != null;
                         messageRef = messagesCleanupQueue.poll()) {
                        messages.remove(messageRef);
                        removedMessages++;
                    }
                    if (removedMessages > 0) {
                        logger.warn("Heap memory was too low to hold all configured messages in the cache. "
                                        + "Removed {} messages from the cache due to memory shortage. "
                                        + "Either increase your heap settings or decrease your message cache settings!",
                                removedMessages);
                    }
                } catch (Throwable t) {
                    logger.error("Failed to clean softly referenced messages!", t);
                } finally {
                    api.getMessageCacheLock().unlock();
                }
            }, 30, 30, TimeUnit.SECONDS);
        } else {
            messagesCleanupFuture = CompletableFuture.completedFuture(null);
        }

    }

    /**
     * Adds a message to the cache.
     *
     * @param message The message to add.
     */
    public void addMessage(Message message) {
        api.getMessageCacheLock().lock();
        try {
            api.addMessageToCache(message);
            if (messages.stream().map(Reference::get).anyMatch(message::equals)) {
                return;
            }
            // Add the message in the correct order
            messages.removeIf(messageRef -> messageRef.get() == null);
            Reference<Message> messageRef = new SoftReference<>(message, messagesCleanupQueue);
            int pos = Collections.binarySearch(messages, messageRef, Comparator.comparing(Reference::get));
            if (pos < 0) {
                pos = -pos - 1;
            }
            messages.add(pos, messageRef);
        } finally {
            api.getMessageCacheLock().unlock();
        }
    }

    /**
     * Adds a message to be cached forever.
     *
     * @param message The message to add.
     */
    public void addCacheForeverMessage(Message message) {
        cacheForeverMessages.add(message);
    }

    /**
     * Removes a message to be cached forever.
     *
     * @param message The message to remove.
     */
    public void removeCacheForeverMessage(Message message) {
        cacheForeverMessages.remove(message);
    }

    /**
     * Removes a message from the cache.
     *
     * @param message The message to remove.
     */
    public void removeMessage(Message message) {
        api.getMessageCacheLock().lock();
        try {
            messages.removeIf(messageRef -> Objects.equals(messageRef.get(), message));
        } finally {
            api.getMessageCacheLock().unlock();
        }
    }

    /**
     * Cleans the cache.
     */
    public void clean() {
        Instant minAge = Instant.now().minus(storageTimeInSeconds, ChronoUnit.SECONDS);
        api.getMessageCacheLock().lock();
        try {
            messages.removeIf(messageRef -> Optional.ofNullable(messageRef.get())
                    .map(message -> !message.isCachedForever() && message.getCreationTimestamp().isBefore(minAge))
                    .orElse(true));
            long foreverCachedAmount = messages.stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .filter(Message::isCachedForever)
                    .count();
            messages.removeAll(messages.stream()
                    .filter(messageRef -> Optional.ofNullable(messageRef.get())
                            .map(message -> !message.isCachedForever())
                            .orElse(true))
                    .limit(Math.max(0, messages.size() - capacity - foreverCachedAmount))
                    .collect(Collectors.toList()));
        } finally {
            api.getMessageCacheLock().unlock();
        }
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = Math.max(capacity, 0);
    }

    @Override
    public int getStorageTimeInSeconds() {
        return storageTimeInSeconds;
    }

    @Override
    public void setStorageTimeInSeconds(int storageTimeInSeconds) {
        this.storageTimeInSeconds = Math.max(storageTimeInSeconds, 0);
    }

    @Override
    public void setAutomaticCleanupEnabled(boolean automaticCleanupEnabled) {
        if (automaticCleanupEnabled) {
            cleanFuture.updateAndGet(future -> {
                // already enabled
                if (future != null) {
                    return future;
                }

                return api.getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                    try {
                        clean();
                    } catch (Throwable t) {
                        logger.error("Failed to clean message cache!", t);
                    }
                }, 1, 1, TimeUnit.MINUTES);
            });
        } else {
            cleanFuture.updateAndGet(future -> {
                if (future != null) {
                    future.cancel(false);
                }
                return null;
            });
        }
    }

    @Override
    public void cleanup() {
        setAutomaticCleanupEnabled(false);
        messagesCleanupFuture.cancel(false);
    }

}
