package org.javacord.core.dto.packet.invite;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InviteDeletePacketData {

    private final String channelId;
    @Nullable
    private final String guildId;
    private final String code;

    @JsonCreator
    public InviteDeletePacketData(String channelId, @Nullable String guildId, String code) {
        this.channelId = channelId;
        this.guildId = guildId;
        this.code = code;
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public String getCode() {
        return code;
    }
}
