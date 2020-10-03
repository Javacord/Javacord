package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageReactionRemoveAllPacketData {

    private final String channelId;
    private final String messageId;
    @Nullable
    private final String guildId;

    @JsonCreator
    public MessageReactionRemoveAllPacketData(String channelId, String messageId, @Nullable String guildId) {
        this.channelId = channelId;
        this.messageId = messageId;
        this.guildId = guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getMessageId() {
        return messageId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }
}
