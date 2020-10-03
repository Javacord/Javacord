package org.javacord.core.dto.packet.invite;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.PartialUserDto;
import org.javacord.core.dto.entity.UserDto;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

public class InviteCreatePacketData {

    private final String channelId;
    private final String code;
    private final OffsetDateTime createdAt;
    @Nullable
    private final String guildId;
    @Nullable
    private final UserDto inviter;
    private final int maxAge;
    private final int maxUses;
    @Nullable
    private final PartialUserDto targetUser;
    @Nullable
    private final Integer targetUserType;
    private final boolean temporary;
    private final int uses;

    @JsonCreator
    public InviteCreatePacketData(String channelId, String code, OffsetDateTime createdAt, @Nullable String guildId, @Nullable UserDto inviter, int maxAge, int maxUses, @Nullable PartialUserDto targetUser, @Nullable Integer targetUserType, boolean temporary, int uses) {
        this.channelId = channelId;
        this.code = code;
        this.createdAt = createdAt;
        this.guildId = guildId;
        this.inviter = inviter;
        this.maxAge = maxAge;
        this.maxUses = maxUses;
        this.targetUser = targetUser;
        this.targetUserType = targetUserType;
        this.temporary = temporary;
        this.uses = uses;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getCode() {
        return code;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public Optional<UserDto> getInviter() {
        return Optional.ofNullable(inviter);
    }

    public Duration getMaxAge() {
        return Duration.ofSeconds(maxAge);
    }

    public int getMaxUses() {
        return maxUses;
    }

    public Optional<PartialUserDto> getTargetUser() {
        return Optional.ofNullable(targetUser);
    }

    public Optional<Integer> getTargetUserType() {
        return Optional.ofNullable(targetUserType);
    }

    public boolean isTemporary() {
        return temporary;
    }

    public int getUses() {
        return uses;
    }
}
