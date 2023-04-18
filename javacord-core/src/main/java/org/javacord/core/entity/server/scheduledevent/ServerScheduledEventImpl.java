package org.javacord.core.entity.server.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventMetadata;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventPrivacyLevel;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventStatus;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventType;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventUser;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ServerScheduledEventImpl implements ServerScheduledEvent {

    private static final Logger logger = LoggerUtil.getLogger(ServerScheduledEventImpl.class);

    private final DiscordApi api;
    private final long id;
    private final long serverId;
    private final Long channelId;
    private final Long creatorId;
    private final String name;
    private final String description;
    private final ServerScheduledEventType serverScheduledEventType;
    private final Long entityId;
    private final ServerScheduledEventStatus status;
    private final ServerScheduledEventPrivacyLevel serverScheduledEventPrivacyLevel;
    private final Instant scheduledStartTime;
    private final Instant scheduledEndTime;
    private final Integer userCount;
    private final ServerScheduledEventMetadata serverScheduledEventMetadata;
    private final String image;
    private final User creator;

    /**
     * Creates a new server scheduled event.
     *
     * @param api  The discord api instance.
     * @param data The json data of the server scheduled event.
     */
    public ServerScheduledEventImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        this.id = data.get("id").asLong();
        this.serverId = data.get("guild_id").asLong();
        this.channelId = data.path("channel_id").longValue();
        this.creatorId = data.path("creator_id").longValue();
        this.name = data.get("name").asText();
        this.description = data.path("description").textValue();
        this.serverScheduledEventType = ServerScheduledEventType.fromValue(data.get("entity_type").asInt());
        this.entityId = data.path("entity_id").longValue();
        this.status = ServerScheduledEventStatus.fromValue(data.get("status").asInt());
        this.serverScheduledEventPrivacyLevel =
                ServerScheduledEventPrivacyLevel.fromValue(data.get("privacy_level").asInt());
        this.scheduledStartTime = OffsetDateTime.parse(data.get("scheduled_start_time").asText()).toInstant();
        this.scheduledEndTime = data.hasNonNull("scheduled_end_time")
                ? OffsetDateTime.parse(data.get("scheduled_end_time").asText()).toInstant()
                : null;
        this.serverScheduledEventMetadata =
                data.hasNonNull("entity_metadata")
                        ? new ServerScheduledEventMetadataImpl(data.get("entity_metadata"))
                        : null;
        this.creator =
                data.hasNonNull("creator") ? new UserImpl(api, data.get("creator"), (MemberImpl) null, null) : null;
        this.image = data.path("image").textValue();
        this.userCount = data.path("user_count").intValue();
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
    public long getServerId() {
        return serverId;
    }

    @Override
    public Optional<Long> getChannelId() {
        return Optional.ofNullable(channelId);
    }

    @Override
    public Optional<Long> getCreatorId() {
        return Optional.ofNullable(creatorId);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Instant getStartTime() {
        return scheduledStartTime;
    }

    @Override
    public Optional<Instant> getEndTime() {
        return Optional.ofNullable(scheduledEndTime);
    }

    @Override
    public ServerScheduledEventPrivacyLevel getPrivacyLevel() {
        return serverScheduledEventPrivacyLevel;
    }

    @Override
    public ServerScheduledEventStatus getStatus() {
        return status;
    }

    @Override
    public ServerScheduledEventType getEntityType() {
        return serverScheduledEventType;
    }

    @Override
    public Optional<Long> getEntityId() {
        return Optional.ofNullable(entityId);
    }

    @Override
    public Optional<ServerScheduledEventMetadata> getEntityMetadata() {
        return Optional.ofNullable(serverScheduledEventMetadata);
    }

    @Override
    public Optional<User> getCreator() {
        return Optional.ofNullable(creator);
    }

    @Override
    public Optional<Integer> getUserCount() {
        return Optional.ofNullable(userCount);
    }

    @Override
    public Optional<Icon> getImage() {
        if (null == image) {
            return Optional.empty();
        }
        try {
            StringBuilder builder =
                    new StringBuilder().append("https://").append(Javacord.DISCORD_CDN_DOMAIN).append("/guild-events/")
                            .append(getId()).append("/").append(image).append(".png");
            return Optional.of(new IconImpl(getApi(), new URL(builder.toString())));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SCHEDULED_EVENTS).setUrlParameters(
                getIdAsString()).setUrlParameters(getIdAsString()).execute(result -> null);
    }

    @Override
    public CompletableFuture<Set<ServerScheduledEventUser>> requestParticipants() {
        CompletableFuture<Set<ServerScheduledEventUser>> future = new CompletableFuture<>();
        ArrayList<ServerScheduledEventUser> users = new ArrayList<>();

        fetchParticipantsPageAndAddAllToCollection(null, users, future);

        return future;
    }

    @Override
    public CompletableFuture<Set<ServerScheduledEventUser>> requestParticipants(Integer limit, Long before,
                                                                                Long after) {
        return new RestRequest<Set<ServerScheduledEventUser>>(getApi(), RestMethod.GET,
                RestEndpoint.SCHEDULED_EVENT_USERS).setUrlParameters(String.valueOf(getServerId()), getIdAsString())
                .addQueryParameter("limit", String.valueOf(limit))
                .addQueryParameter("after", after != null ? String.valueOf(after) : null)
                .addQueryParameter("before", before != null ? String.valueOf(before) : null)
                .addQueryParameter("with_member", String.valueOf(true)).execute(result -> {
                    Set<ServerScheduledEventUser> users = new HashSet<>();
                    for (JsonNode jsonNode : result.getJsonBody()) {
                        users.add(new ServerScheduledEventUserImpl(this, jsonNode));
                    }
                    return users;
                });
    }

    private void fetchParticipantsPageAndAddAllToCollection(Long after, ArrayList<ServerScheduledEventUser> userList,
                                                            CompletableFuture<Set<ServerScheduledEventUser>>
                                                                    futureToComplete) {
        this.requestParticipants(null, null, after).thenAccept((page) -> {
            userList.addAll(page);

            // If the response was smaller than 100 entries, this was the last page.
            // Let's pass the full list to the future!
            if (page.size() < 100) {
                futureToComplete.complete(Collections.unmodifiableSet(new HashSet<>(userList)));
                return;
            }

            // The response contained 100 bans. There could be more, so let's request the next page
            fetchParticipantsPageAndAddAllToCollection(userList.get(userList.size() - 1).getUser().getId(), userList,
                    futureToComplete);
        }).exceptionally(t -> {
            futureToComplete.completeExceptionally(t);
            return null;
        });
    }

}
