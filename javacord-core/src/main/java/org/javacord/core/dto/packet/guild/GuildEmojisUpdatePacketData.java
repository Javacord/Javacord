package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.EmojiDto;

public class GuildEmojisUpdatePacketData {

    private final String guildId;
    private final EmojiDto[] emojis;

    @JsonCreator
    public GuildEmojisUpdatePacketData(String guildId, EmojiDto[] emojis) {
        this.guildId = guildId;
        this.emojis = emojis;
    }

    public String getGuildId() {
        return guildId;
    }

    public EmojiDto[] getEmojis() {
        return emojis;
    }
}
