package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GuildRoleDeletePacketData {

    private final String guildId;
    private final String roleId;

    @JsonCreator
    public GuildRoleDeletePacketData(String guildId, String roleId) {
        this.guildId = guildId;
        this.roleId = roleId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getRoleId() {
        return roleId;
    }
}
