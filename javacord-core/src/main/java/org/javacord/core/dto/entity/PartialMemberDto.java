package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class PartialMemberDto {

    @Nullable
    private final UserDto user;
    @Nullable
    private final String nick;
    @Nullable
    @JsonProperty("roles")
    private final String[] roleIds;
    @Nullable
    private final Instant joinedAt;
    @Nullable
    private final Instant premiumSince;
    @Nullable
    private final Boolean deaf;
    @Nullable
    private final Boolean mute;

    @JsonCreator
    public PartialMemberDto(@Nullable UserDto user, @Nullable String nick, @Nullable String[] roleIds, @Nullable Instant joinedAt, @Nullable Instant premiumSince, @Nullable Boolean deaf, @Nullable Boolean mute) {
        this.user = user;
        this.nick = nick;
        this.roleIds = roleIds;
        this.joinedAt = joinedAt;
        this.premiumSince = premiumSince;
        this.deaf = deaf;
        this.mute = mute;
    }

    public Optional<UserDto> getUser() {
        return Optional.ofNullable(user);
    }

    public Optional<String> getNick() {
        return Optional.ofNullable(nick);
    }

    public Optional<String[]> getRoleIds() {
        return Optional.ofNullable(roleIds);
    }

    public Optional<Instant> getJoinedAt() {
        return Optional.ofNullable(joinedAt);
    }

    public Optional<Instant> getPremiumSince() {
        return Optional.ofNullable(premiumSince);
    }

    public Optional<Boolean> getDeaf() {
        return Optional.ofNullable(deaf);
    }

    public Optional<Boolean> getMute() {
        return Optional.ofNullable(mute);
    }
}
