package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VoiceStateDto {

    @Nullable
    private final String guildId;
    @Nullable
    private final String channelId;
    private final String userId;
    @Nullable
    private final MemberDto member;
    private final String sessionId;
    private final boolean deaf;
    private final boolean mute;
    private final boolean selfDeaf;
    private final boolean selfMute;
    @Nullable
    private final Boolean selfStream;
    private final boolean selfVideo;
    private final boolean suppress;

    @JsonCreator
    public VoiceStateDto(@Nullable String guildId, @Nullable String channelId, String userId, @Nullable MemberDto member, String sessionId, boolean deaf, boolean mute, boolean selfDeaf, boolean selfMute, @Nullable Boolean selfStream, boolean selfVideo, boolean suppress) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.userId = userId;
        this.member = member;
        this.sessionId = sessionId;
        this.deaf = deaf;
        this.mute = mute;
        this.selfDeaf = selfDeaf;
        this.selfMute = selfMute;
        this.selfStream = selfStream;
        this.selfVideo = selfVideo;
        this.suppress = suppress;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public Optional<String> getChannelId() {
        return Optional.ofNullable(channelId);
    }

    public String getUserId() {
        return userId;
    }

    public Optional<MemberDto> getMember() {
        return Optional.ofNullable(member);
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isDeaf() {
        return deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public boolean isSelfDeaf() {
        return selfDeaf;
    }

    public boolean isSelfMute() {
        return selfMute;
    }

    public Optional<Boolean> getSelfStream() {
        return Optional.ofNullable(selfStream);
    }

    public boolean isSelfVideo() {
        return selfVideo;
    }

    public boolean isSuppress() {
        return suppress;
    }
}
