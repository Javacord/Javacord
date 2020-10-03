package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class MemberDto {

    @Nullable
    private final UserDto user;
    @Nullable
    private final String nick;
    @JsonProperty("roles")
    private final String[] roleIds;
    private final Instant joinedAt;
    @Nullable
    private final Instant premiumSince;
    private final boolean deaf;
    private final boolean mute;

    public MemberDto(@Nullable UserDto user, @Nullable String nick, String[] roleIds, Instant joinedAt, @Nullable Instant premiumSince, boolean deaf, boolean mute) {
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

    public String[] getRoleIds() {
        return roleIds;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public Optional<Instant> getPremiumSince() {
        return Optional.ofNullable(premiumSince);
    }

    public boolean isDeaf() {
        return deaf;
    }

    public boolean isMute() {
        return mute;
    }
}
