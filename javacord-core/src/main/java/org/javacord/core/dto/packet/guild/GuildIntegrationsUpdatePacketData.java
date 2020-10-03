package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GuildIntegrationsUpdatePacketData {

    private final String guildId;

    @JsonCreator
    public GuildIntegrationsUpdatePacketData(String guildId) {
        this.guildId = guildId;
    }

    public String getGuildId() {
        return guildId;
    }
}
