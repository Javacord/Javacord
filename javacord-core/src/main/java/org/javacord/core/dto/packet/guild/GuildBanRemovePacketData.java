package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.UserDto;

public class GuildBanRemovePacketData {

    private final String guildId;
    private final UserDto user;

    @JsonCreator
    public GuildBanRemovePacketData(String guildId, UserDto user) {
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
