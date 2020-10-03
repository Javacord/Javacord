package org.javacord.core.dto.packet.presence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.javacord.core.dto.entity.MemberDto;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TypingStartPacketData {

    private final String channelId;
    @Nullable
    private final String guildId;
    private final String userId;
    @JsonProperty("timestamp")
    private final long timestampSeconds;
    @Nullable
    private final MemberDto member;

    @JsonCreator
    public TypingStartPacketData(String channelId, @Nullable String guildId, String userId, long timestampSeconds, @Nullable MemberDto member) {
        this.channelId = channelId;
        this.guildId = guildId;
        this.userId = userId;
        this.timestampSeconds = timestampSeconds;
        this.member = member;
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestampSeconds() {
        return timestampSeconds;
    }

    public Optional<MemberDto> getMember() {
        return Optional.ofNullable(member);
    }
}
