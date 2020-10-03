package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class ChannelDto {

    private final String id;
    private final int type;
    @Nullable
    private final String guildId;
    @Nullable
    private final Integer position;
    @Nullable
    private final OverwriteDto permissionOverwrites;
    @Nullable
    private final String name;
    @Nullable
    private final String topic;
    @Nullable
    private final Boolean nsfw;
    @Nullable
    private final String lastMessageId;
    @Nullable
    private final Integer bitrate;
    @Nullable
    private final Integer userLimit;
    @Nullable
    @JsonProperty("rate_limit_per_user")
    private final Integer rateLimitPerUserSeconds;
    @Nullable
    private final UserDto[] recipients;
    @Nullable
    @JsonProperty("icon")
    private final String iconHash;
    @Nullable
    private final String ownerId;
    @Nullable
    private final String applicationId;
    @Nullable
    private final String parentId;
    @Nullable
    private final Instant lastPinTimestamo;

    @JsonCreator
    public ChannelDto(String id, int type, @Nullable String guildId, @Nullable Integer position, OverwriteDto permissionOverwrites, @Nullable String name, @Nullable String topic, @Nullable Boolean nsfw, @Nullable String lastMessageId, @Nullable Integer bitrate, @Nullable Integer userLimit, @Nullable Integer rateLimitPerUserSeconds, @Nullable UserDto[] recipients, @Nullable String iconHash, @Nullable String ownerId, @Nullable String applicationId, @Nullable String parentId, @Nullable Instant lastPinTimestamo) {
        this.id = id;
        this.type = type;
        this.guildId = guildId;
        this.position = position;
        this.permissionOverwrites = permissionOverwrites;
        this.name = name;
        this.topic = topic;
        this.nsfw = nsfw;
        this.lastMessageId = lastMessageId;
        this.bitrate = bitrate;
        this.userLimit = userLimit;
        this.rateLimitPerUserSeconds = rateLimitPerUserSeconds;
        this.recipients = recipients;
        this.iconHash = iconHash;
        this.ownerId = ownerId;
        this.applicationId = applicationId;
        this.parentId = parentId;
        this.lastPinTimestamo = lastPinTimestamo;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public Optional<Integer> getPosition() {
        return Optional.ofNullable(position);
    }

    public Optional<OverwriteDto> getPermissionOverwrites() {
        return Optional.ofNullable(permissionOverwrites);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getTopic() {
        return Optional.ofNullable(topic);
    }

    public Optional<Boolean> getNsfw() {
        return Optional.ofNullable(nsfw);
    }

    public Optional<String> getLastMessageId() {
        return Optional.ofNullable(lastMessageId);
    }

    public Optional<Integer> getBitrate() {
        return Optional.ofNullable(bitrate);
    }

    public Optional<Integer> getUserLimit() {
        return Optional.ofNullable(userLimit);
    }

    public Optional<Integer> getRateLimitPerUserSeconds() {
        return Optional.ofNullable(rateLimitPerUserSeconds);
    }

    public Optional<UserDto[]> getRecipients() {
        return Optional.ofNullable(recipients);
    }

    public Optional<String> getIconHash() {
        return Optional.ofNullable(iconHash);
    }

    public Optional<String> getOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public Optional<String> getApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public Optional<String> getParentId() {
        return Optional.ofNullable(parentId);
    }

    public Optional<Instant> getLastPinTimestamo() {
        return Optional.ofNullable(lastPinTimestamo);
    }
}
