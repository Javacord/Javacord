package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class ActivityDto {

    private final String name;
    private final int type;
    @Nullable
    private final String url;
    private final Instant createdAt;
    @Nullable
    private final ActivityTimestampsDto timestamps;
    @Nullable
    private final String applicationId;
    @Nullable
    private final String details;
    @Nullable
    private final String state;
    @Nullable
    private final ActivityEmojiDto emoji;
    @Nullable
    private final ActivityPartyDto party;
    @Nullable
    private final ActivityAssetsDto assets;
    @Nullable
    private final ActivitySecretsDto secrets;
    @Nullable
    private final Boolean instance;
    @Nullable
    private final Integer flags;

    @JsonCreator
    public ActivityDto(String name, int type, @Nullable String url, Instant createdAt, @Nullable ActivityTimestampsDto timestamps, @Nullable String applicationId, @Nullable String details, @Nullable String state, @Nullable ActivityEmojiDto emoji, @Nullable ActivityPartyDto party, @Nullable ActivityAssetsDto assets, @Nullable ActivitySecretsDto secrets, @Nullable Boolean instance, @Nullable Integer flags) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.createdAt = createdAt;
        this.timestamps = timestamps;
        this.applicationId = applicationId;
        this.details = details;
        this.state = state;
        this.emoji = emoji;
        this.party = party;
        this.assets = assets;
        this.secrets = secrets;
        this.instance = instance;
        this.flags = flags;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Optional<ActivityTimestampsDto> getTimestamps() {
        return Optional.ofNullable(timestamps);
    }

    public Optional<String> getApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    public Optional<ActivityEmojiDto> getEmoji() {
        return Optional.ofNullable(emoji);
    }

    public Optional<ActivityPartyDto> getParty() {
        return Optional.ofNullable(party);
    }

    public Optional<ActivityAssetsDto> getAssets() {
        return Optional.ofNullable(assets);
    }

    public Optional<ActivitySecretsDto> getSecrets() {
        return Optional.ofNullable(secrets);
    }

    public Optional<Boolean> getInstance() {
        return Optional.ofNullable(instance);
    }

    public Optional<Integer> getFlags() {
        return Optional.ofNullable(flags);
    }
}
