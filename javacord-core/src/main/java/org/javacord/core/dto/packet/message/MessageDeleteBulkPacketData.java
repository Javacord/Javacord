package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageDeleteBulkPacketData {

    private final String[] ids;
    private final String channelId;
    @Nullable
    private final String guildId;

    @JsonCreator
    public MessageDeleteBulkPacketData(String[] ids, String channelId, @Nullable String guildId) {
        this.ids = ids;
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public String[] getIds() {
        return ids;
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }
}
