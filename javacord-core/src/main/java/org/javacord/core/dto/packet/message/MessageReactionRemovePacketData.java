package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.PartialEmojiDto;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageReactionRemovePacketData {

    private final String userId;
    private final String channelId;
    private final String messageId;
    @Nullable
    private final String guildId;
    private final PartialEmojiDto emoji;

    @JsonCreator
    public MessageReactionRemovePacketData(String userId, String channelId, String messageId, @Nullable String guildId, PartialEmojiDto emoji) {
        this.userId = userId;
        this.channelId = channelId;
        this.messageId = messageId;
        this.guildId = guildId;
        this.emoji = emoji;
    }

    public String getUserId() {
        return userId;
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

    public PartialEmojiDto getEmoji() {
        return emoji;
    }

}
