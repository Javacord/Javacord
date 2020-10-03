package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageDeletePacketData {

    private final String id;
    private final String channelId;
    @Nullable
    private final String guildId;

    @JsonCreator
    public MessageDeletePacketData(String id, String channelId, @Nullable String guildId) {
        this.id = id;
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }
}
