package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageReferenceDto {

    @Nullable
    private final String messageId;
    private final String channelId;
    @Nullable
    private final String guildId;

    @JsonCreator
    public MessageReferenceDto(@Nullable String messageId, String channelId, @Nullable String guildId) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public Optional<String> getMessageId() {
        return Optional.ofNullable(messageId);
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }
}
