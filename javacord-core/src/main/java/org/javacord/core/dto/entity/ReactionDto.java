package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReactionDto {

    private final int count;
    private final boolean me;
    private final PartialEmojiDto emoji;

    @JsonCreator
    public ReactionDto(int count, boolean me, PartialEmojiDto emoji) {
        this.count = count;
        this.me = me;
        this.emoji = emoji;
    }

    public int getCount() {
        return count;
    }

    public boolean isMe() {
        return me;
    }

    public PartialEmojiDto getEmoji() {
        return emoji;
    }
}
