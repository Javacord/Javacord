package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.listener.channel.user.InternalPrivateChannelAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;

import java.util.Objects;

/**
 * The implementation of {@link PrivateChannel}.
 */
public class PrivateChannelImpl implements PrivateChannel, Cleanupable, InternalTextChannel,
                                           InternalPrivateChannelAttachableListenerManager {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The recipient of the private channel.
     */
    private final UserImpl recipient;

    /**
     * The message cache of the private channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param data The json data of the channel.
     */
    public PrivateChannelImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        this.recipient = (UserImpl) api.getOrCreateUser(data.get("recipients").get(0));
        this.messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds(),
                api.isDefaultAutomaticMessageCacheCleanupEnabled());

        id = Long.parseLong(data.get("id").asText());
        recipient.setChannel(this);
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public User getRecipient() {
        return recipient;
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public void cleanup() {
        messageCache.cleanup();
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("PrivateChannel (id: %s, recipient: %s)", getIdAsString(), getRecipient());
    }

}
