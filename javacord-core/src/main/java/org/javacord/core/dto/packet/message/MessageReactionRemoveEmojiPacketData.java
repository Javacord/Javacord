package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.PartialEmojiDto;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageReactionRemoveEmojiPacketData {

    private final String channelId;
    @Nullable
    private final String guildId;
    private final String messageId;
    private final PartialEmojiDto emoji;

    @JsonCreator
    public MessageReactionRemoveEmojiPacketData(String channelId, @Nullable String guildId, String messageId, PartialEmojiDto emoji) {
        this.channelId = channelId;
        this.guildId = guildId;
        this.messageId = messageId;
        this.emoji = emoji;
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public String getMessageId() {
        return messageId;
    }

    public PartialEmojiDto getEmoji() {
        return emoji;
    }
}
