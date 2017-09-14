package de.btobastian.javacord.utils.cache;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The implementation of {@link MessageCache}.
 */
public class ImplMessageCache implements MessageCache {

    /**
     * A list with all messages.
     */
    private final List<Message> messages = new ArrayList<>();

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The maximum amount of stored messages.
     */
    private int capacity;

    /**
     * The time how long messages should be cached.
     */
    private int storageTimeInSeconds;

    /**
     * Creates a new message cache.
     *
     * @param api The discord api instance.
     * @param capacity The capacity of the cache, not including messages which are cached forever.
     * @param storageTimeInSeconds The storage time in seconds.
     */
    public ImplMessageCache(DiscordApi api, int capacity, int storageTimeInSeconds) {
        this.api = (ImplDiscordApi) api;
        this.capacity = capacity;
        this.storageTimeInSeconds = storageTimeInSeconds;

        api.getThreadPool().getScheduler().scheduleWithFixedDelay(this::clean, 1, 1, TimeUnit.MINUTES);
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
                pos = -pos-1;
            }
            messages.add(pos, message);
        }
    }

    /**
     * Cleans the cache.
     */
    public void clean() {
        Instant minAge = Instant.ofEpochMilli(System.currentTimeMillis() - storageTimeInSeconds * 100);
        synchronized (messages) {
            long foreverCachedAmount = messages.stream().filter(Message::isCachedForever).count();
            Iterator<Message> iterator = messages.iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                if (message.isCachedForever()) {
                    continue;
                }
                if (messages.size() > capacity + foreverCachedAmount || message.getCreationDate().isBefore(minAge)) {
                    api.removeMessageFromCache(message.getId());
                    iterator.remove();
                } else {
                    // No need to keep iterating as the list is ordered.
                    // If we don't remove an element now, we will also not remove the next element.
                    return;
                }
            }
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

}
