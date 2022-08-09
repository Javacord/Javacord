package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.server.*;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

public class ScheduledEventImpl implements ScheduledEvent {
    private final DiscordApi api;
    private final Long id;
    private final Long serverId;
    private final Long channelId;
    private final Long creatorId;
    private final String name;
    private final String description;
    private final Instant startTime;
    private final Instant endTime;
    private final PrivacyLevel privacyLevel;
    private final ScheduledEventStatus status;
    private final ScheduledEventType eventType;
    private final Long entityId;
    private final String location;
    private final User creator;
    private final Integer userCount;
    private final String imageHash;

    public ScheduledEventImpl(DiscordApi api, JsonNode jsonBody) {
        this.api = api;

        this.id = jsonBody.get("id").asLong();
        this.serverId = jsonBody.get("guild_id").asLong();

        this.channelId = jsonBody.hasNonNull("channel_id") ? jsonBody.get("channel_id").asLong() : null;
        this.creatorId = jsonBody.hasNonNull("creator_id") ? jsonBody.get("creator_id").asLong() : null;

        this.name = jsonBody.get("name").asText();
        this.description = jsonBody.hasNonNull("description") ? jsonBody.get("description").asText() : null;

        this.startTime = OffsetDateTime.parse((jsonBody.get("scheduled_start_time").asText())).toInstant();
        this.endTime = jsonBody.hasNonNull("end_time") ? OffsetDateTime.parse((jsonBody.get("scheduled_end_time").asText())).toInstant() : null;

        this.privacyLevel = PrivacyLevel.fromId(jsonBody.get("privacy_level").asInt());
        this.status = ScheduledEventStatus.fromId(jsonBody.get("status").asInt());
        this.eventType = ScheduledEventType.fromId(jsonBody.get("entity_type").asInt());

        this.entityId = jsonBody.hasNonNull("entity_id") ? jsonBody.get("entity_id").asLong() : null;
        this.location = jsonBody.hasNonNull("entity_metadata") && jsonBody.get("entity_metadata").hasNonNull("location") ? jsonBody.get("entity_metadata").get("location").asText() : null;

        this.creator = jsonBody.hasNonNull("creator") ? new UserImpl((DiscordApiImpl) api, jsonBody.get("user"), (MemberImpl) null, null) : null;
        this.userCount = jsonBody.hasNonNull("user_count") ? jsonBody.get("user_count").asInt() : null;
        this.imageHash = jsonBody.hasNonNull("image") ? jsonBody.get("image_hash").asText() : null;
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
    public String getName() {
        return name;
    }

    @Override
    public Long getServerId() {
        return serverId;
    }

    @Override
    public Optional<Long> getChannelId() {
        return Optional.ofNullable(channelId);
    }

    @Override
    public Optional<User> getCreator() {
        return Optional.ofNullable(creator);
    }

    @Override
    public Optional<Long> getCreatorId() {
        return Optional.ofNullable(creatorId);
    }

    @Override
    public Optional<Long> getEntityId() {
        return Optional.ofNullable(entityId);
    }

    @Override
    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public Optional<Instant> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    @Override
    public ScheduledEventStatus getStatus() {
        return status;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public ScheduledEventType getType() {
        return eventType;
    }

    @Override
    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

    @Override
    public Optional<Integer> getUserCount() {
        return Optional.ofNullable(userCount);
    }

    @Override
    public Optional<Icon> getCoverImage() {
        if (imageHash == null) {
            return Optional.empty();
        }
        StringBuilder url = new StringBuilder("https://" + Javacord.DISCORD_CDN_DOMAIN + "/")
                .append("guilds-events/").append(id).append('/')
                .append(imageHash).append(".png");
        try {
            return Optional.of(new IconImpl(api, new URL(url.toString())));
        } catch (MalformedURLException e) {
            throw new AssertionError("Found a event cover image url. Please update to the latest Javacord "
                    + "version or create an issue on GitHub if you are already using the latest one.");
        }
    }

    @Override
    public Optional<String> getCoverImageHash() {
        return Optional.ofNullable(imageHash);
    }
}
