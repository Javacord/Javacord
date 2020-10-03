package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.MemberDto;
import org.javacord.core.dto.entity.PartialEmojiDto;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageReactionAddPacketData {

    private final String userId;
    private final String channelId;
    private final String messageId;
    @Nullable
    private final String guildId;
    @Nullable
    private final MemberDto member;
    private final PartialEmojiDto emoji;

    @JsonCreator
    public MessageReactionAddPacketData(String userId, String channelId, String messageId, @Nullable String guildId, @Nullable MemberDto member, PartialEmojiDto emoji) {
        this.userId = userId;
        this.channelId = channelId;
        this.messageId = messageId;
        this.guildId = guildId;
        this.member = member;
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

    public Optional<MemberDto> getMember() {
        return Optional.ofNullable(member);
    }

    public PartialEmojiDto getEmoji() {
        return emoji;
    }
}
