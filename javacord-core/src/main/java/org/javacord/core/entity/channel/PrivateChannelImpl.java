package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.user.PrivateChannelCreateEvent;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.channel.user.PrivateChannelCreateEventImpl;
import org.javacord.core.listener.channel.user.InternalPrivateChannelAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;

import java.util.Objects;
import java.util.Optional;

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
    private UserImpl recipient;

    /**
     * The id of the recipient of the private channel.
     */
    private Long recipientId;

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
        this(api, data.get("id").asLong(),
                new UserImpl(api, data.get("recipients").get(0), (MemberImpl) null, null),
                data.get("recipients").get(0).get("id").asLong());
    }

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param channelId The id of the channel.
     * @param recipient The recipient of the channel.
     * @param recipientId The id of the recipient of the channel.
     */
    public PrivateChannelImpl(DiscordApiImpl api, String channelId, UserImpl recipient, Long recipientId) {
        this(api, Long.parseLong(channelId), recipient, recipientId);
    }

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param channelId The id of the channel.
     * @param recipient The recipient of the channel.
     * @param recipientId The id of the recipient of the channel.
     */
    public PrivateChannelImpl(DiscordApiImpl api, long channelId, UserImpl recipient, Long recipientId) {
        this.api = api;
        this.recipient = recipient;
        this.recipientId = recipientId;
        messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds(),
                api.isDefaultAutomaticMessageCacheCleanupEnabled());

        id = channelId;

        api.addChannelToCache(this);
    }

    private void updateRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    private void updateRecipient(UserImpl recipient) {
        if (this.recipientId == recipient.getId()) {
            this.recipient = recipient;
        }
    }

    /**
     * This function is used internally by Javacord to get create and update private channels.
     * It checks if the recipient is known and dispatches a private channel create event if the channel got created.
     *
     * @param api The discord api instance.
     * @param channelId The id of the channel to get.
     * @param userId The id of the recipient of the channel or yourself.
     * @param user The user with the user id.
     *
     * @return The private channel with the id.
     */
    public static PrivateChannelImpl getOrCreatePrivateChannel(DiscordApiImpl api, long channelId, long userId,
                                                               UserImpl user) {
        Optional<PrivateChannel> optionalChannel = api.getPrivateChannelById(channelId);
        if (optionalChannel.isPresent()) {
            // if necessary update private channel information
            PrivateChannelImpl channel = (PrivateChannelImpl) optionalChannel.get();
            if (!channel.getRecipientId().isPresent() && userId != api.getYourself().getId()) {
                channel.updateRecipientId(userId);
            }
            if (!channel.getRecipient().isPresent()) {
                UserImpl recipient;
                if (user == null) {
                    recipient = (UserImpl) api.getCachedUserById(userId).orElse(null);
                } else {
                    recipient = user;
                }
                if (recipient != null && !recipient.isYourself()) {
                    channel.updateRecipient(recipient);
                }
            }
            return channel;
        }

        if (userId == api.getYourself().getId()) {
            return dispatchPrivateChannelCreateEvent(api, new PrivateChannelImpl(api, channelId, null, null));
        }

        UserImpl recipient;
        if (user == null) {
            recipient = (UserImpl) api.getCachedUserById(userId).orElse(null);
        } else {
            recipient = user;
        }
        return dispatchPrivateChannelCreateEvent(api, new PrivateChannelImpl(api, channelId, recipient, userId));
    }

    /**
     * This function creates and dispatches a private channel create event with the given private channel.
     *
     * @param api The discord api instance used to dispatch the event.
     * @param privateChannel The private channel.
     *
     * @return The given private channel to make things easier.
     */
    public static PrivateChannelImpl dispatchPrivateChannelCreateEvent(DiscordApiImpl api,
                                                                        PrivateChannelImpl privateChannel) {
        // dispatch PrivateChannelCreateEvent
        PrivateChannelCreateEvent event = new PrivateChannelCreateEventImpl(privateChannel);
        api.getEventDispatcher()
                .dispatchPrivateChannelCreateEvent(api, privateChannel.getRecipient().orElse(null), event);
        return privateChannel;
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
    public Optional<User> getRecipient() {
        return Optional.ofNullable(recipient);
    }

    @Override
    public Optional<Long> getRecipientId() {
        return Optional.ofNullable(recipientId);
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
