package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.textable.InternalTextableRegularServerChannelAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;

import java.util.Optional;

public class TextableRegularServerChannelImpl extends RegularServerChannelImpl
        implements TextableRegularServerChannel, InternalTextChannel, Cleanupable,
        InternalTextableRegularServerChannelAttachableListenerManager {
    /**
     * The message cache of the server text channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * The slowmode delay of the channel.
     */
    private volatile int delay;

    /**
     * Whether the channel is "not safe for work" or not.
     */
    private volatile boolean nsfw;

    /**
     * The parent id of the channel.
     */
    private volatile long parentId;

    /**
     * Creates a new server channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public TextableRegularServerChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
        delay = data.has("rate_limit_per_user") ? data.get("rate_limit_per_user").asInt(0) : 0;
        parentId = Long.parseLong(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");
        messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds(),
                api.isDefaultAutomaticMessageCacheCleanupEnabled());
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    /**
     * Sets the nsfw flag.
     *
     * @param nsfw The nsfw flag.
     */
    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    /**
     * Sets the parent id of the channel.
     *
     * @param parentId The parent id to set.
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * Sets the slowmode delay of the channel.
     *
     * @param delay The delay in seconds.
     */
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
    }

    /**
     * Gets the slowmode delay of the channel.
     *
     * @return The delay in seconds.
     */
    public int getSlowmodeDelayInSeconds() {
        return delay;
    }

    @Override
    public void cleanup() {
        messageCache.cleanup();
    }

    @Override
    public RegularServerChannel getPermissionableChannel() {
        return this;
    }
}
