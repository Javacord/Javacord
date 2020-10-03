package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.UserDto;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class GuildMemberUpdatePacketData {

    private final String guildId;
    private final String[] roles;
    private final UserDto user;
    @Nullable
    private final String nick;
    private final Instant joinedAt;
    @Nullable
    private final Instant premiumSince;

    @JsonCreator
    public GuildMemberUpdatePacketData(String guildId, String[] roles, UserDto user, @Nullable String nick, Instant joinedAt, @Nullable Instant premiumSince) {
        this.guildId = guildId;
        this.roles = roles;
        this.user = user;
        this.nick = nick;
        this.joinedAt = joinedAt;
        this.premiumSince = premiumSince;
    }

    public String getGuildId() {
        return guildId;
    }

    public String[] getRoles() {
        return roles;
    }

    public UserDto getUser() {
        return user;
    }

    public Optional<String> getNick() {
        return Optional.ofNullable(nick);
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public Optional<Instant> getPremiumSince() {
        return Optional.ofNullable(premiumSince);
    }
}
