package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.ArchivedThreads;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ArchivedThreadsImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.text.InternalServerTextChannelAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ServerTextChannelImpl extends RegularServerChannelImpl
        implements ServerTextChannel, Cleanupable, InternalTextChannel,
        InternalServerTextChannelAttachableListenerManager {

    /**
     * The message cache of the server text channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * Whether the channel is "not safe for work" or not.
     */
    private volatile boolean nsfw;

    /**
     * The parent id of the channel.
     */
    private volatile long parentId;

    /**
     * The topic of the channel.
     */
    private volatile String topic;

    /**
     * The slowmode delay of the channel.
     */
    private volatile int delay;

    /**
     * The default auto archive duration for this channel.
     */
    private volatile int defaultAutoArchiveDuration;

    /**
     * Creates a new server text channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public ServerTextChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
        parentId = Long.parseLong(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");
        topic = data.has("topic") && !data.get("topic").isNull() ? data.get("topic").asText() : "";
        delay = data.has("rate_limit_per_user") ? data.get("rate_limit_per_user").asInt(0) : 0;
        defaultAutoArchiveDuration = data.has("default_auto_archive_duration")
                ? data.get("default_auto_archive_duration").asInt()
                : 1440;

        messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds(),
                api.isDefaultAutomaticMessageCacheCleanupEnabled());
    }

    /**
     * Sets the topic of the channel.
     *
     * @param topic The new topic of the channel.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Sets the nsfw flag.
     *
     * @param nsfw The nsfw flag.
     */
    public void setNsfwFlag(boolean nsfw) {
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

    @Override
    public int getSlowmodeDelayInSeconds() {
        return delay;
    }

    @Override
    public CompletableFuture<ArchivedThreads> getPublicArchivedThreads(Long before, Integer limit) {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (before != null) {
            body.put("before", before);
        }
        if (limit != null) {
            body.put("limit", limit);
        }
        return new RestRequest<ArchivedThreads>(getApi(), RestMethod.GET, RestEndpoint.LIST_PUBLIC_ARCHIVED_THREADS)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .execute(result -> new ArchivedThreadsImpl((DiscordApiImpl) getApi(), (ServerImpl) getServer(),
                        result.getJsonBody()));
    }

    @Override
    public CompletableFuture<ArchivedThreads> getPrivateArchivedThreads(Long before, Integer limit) {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (before != null) {
            body.put("before", before);
        }
        if (limit != null) {
            body.put("limit", limit);
        }
        return new RestRequest<ArchivedThreads>(getApi(), RestMethod.GET, RestEndpoint.LIST_PRIVATE_ARCHIVED_THREADS)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .execute(result -> new ArchivedThreadsImpl((DiscordApiImpl) getApi(), (ServerImpl) getServer(),
                        result.getJsonBody()));
    }

    @Override
    public CompletableFuture<ArchivedThreads> getJoinedPrivateArchivedThreads(Long before, Integer limit) {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (before != null) {
            body.put("before", before);
        }
        if (limit != null) {
            body.put("limit", limit);
        }
        return new RestRequest<ArchivedThreads>(getApi(), RestMethod.GET,
                RestEndpoint.LIST_JOINED_PRIVATE_ARCHIVED_THREADS)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .execute(result -> new ArchivedThreadsImpl((DiscordApiImpl) getApi(), (ServerImpl) getServer(),
                        result.getJsonBody()));
    }

    /**
     * Sets the default auto archive duration for newly created threads in this channel.
     *
     * @param defaultAutoArchiveDuration The auto archive duration in seconds.
     */
    public void setDefaultAutoArchiveDuration(final int defaultAutoArchiveDuration) {
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
    }

    @Override
    public int getDefaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    @Override
    public String getTopic() {
        return topic;
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
        return String.format("ServerTextChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}
