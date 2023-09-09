package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.ArchivedThreads;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ArchivedThreadsImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.text.InternalServerTextChannelAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ServerTextChannelImpl extends TextableRegularServerChannelImpl
        implements ServerTextChannel, InternalServerTextChannelAttachableListenerManager {

    /**
     * The topic of the channel.
     */
    private volatile String topic;

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
        topic = data.has("topic") && !data.get("topic").isNull() ? data.get("topic").asText() : "";
        defaultAutoArchiveDuration = data.has("default_auto_archive_duration")
                ? data.get("default_auto_archive_duration").asInt()
                : 1440;
    }

    /**
     * Sets the topic of the channel.
     *
     * @param topic The new topic of the channel.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public CompletableFuture<ArchivedThreads> getPublicArchivedThreads(Instant before, Integer limit) {
        RestRequest<ArchivedThreads> request =
                new RestRequest<ArchivedThreads>(getApi(), RestMethod.GET, RestEndpoint.LIST_PUBLIC_ARCHIVED_THREADS)
                    .setUrlParameters(getIdAsString());
        if (before != null) {
            request.addQueryParameter("before",
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC)).format(before));
        }
        if (limit != null) {
            request.addQueryParameter("limit", limit.toString());
        }
        return request.execute(result -> new ArchivedThreadsImpl((DiscordApiImpl) getApi(), (ServerImpl) getServer(),
            result.getJsonBody()));
    }

    @Override
    public CompletableFuture<ArchivedThreads> getPrivateArchivedThreads(Instant before, Integer limit) {
        RestRequest<ArchivedThreads> request =
                new RestRequest<ArchivedThreads>(getApi(), RestMethod.GET, RestEndpoint.LIST_PRIVATE_ARCHIVED_THREADS)
                    .setUrlParameters(getIdAsString());
        if (before != null) {
            request.addQueryParameter("before",
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC)).format(before));
        }
        if (limit != null) {
            request.addQueryParameter("limit", limit.toString());
        }
        return request.execute(result -> new ArchivedThreadsImpl((DiscordApiImpl) getApi(), (ServerImpl) getServer(),
            result.getJsonBody()));
    }

    @Override
    public CompletableFuture<ArchivedThreads> getJoinedPrivateArchivedThreads(Long before, Integer limit) {
        RestRequest<ArchivedThreads> request = new RestRequest<ArchivedThreads>(
                getApi(), RestMethod.GET, RestEndpoint.LIST_JOINED_PRIVATE_ARCHIVED_THREADS)
                    .setUrlParameters(getIdAsString());
        if (before != null) {
            request.addQueryParameter("before", before.toString());
        }
        if (limit != null) {
            request.addQueryParameter("limit", limit.toString());
        }
        return request.execute(result -> new ArchivedThreadsImpl((DiscordApiImpl) getApi(), (ServerImpl) getServer(),
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
    public String getTopic() {
        return topic;
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
