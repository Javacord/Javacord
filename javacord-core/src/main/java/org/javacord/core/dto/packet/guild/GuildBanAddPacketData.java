package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.UserDto;

public class GuildBanAddPacketData {

    private final String guildId;
    private final UserDto user;

    @JsonCreator
    public GuildBanAddPacketData(String guildId, UserDto user) {
        this.guildId = guildId;
        this.user = user;
    }

    public String getGuildId() {
        return guildId;
    }

    public UserDto getUser() {
        return user;
    }
}
