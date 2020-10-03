package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.RoleDto;

public class GuildRoleCreatePacketData {

    private final String guildId;
    private final RoleDto role;

    @JsonCreator
    public GuildRoleCreatePacketData(String guildId, RoleDto role) {
        this.guildId = guildId;
        this.role = role;
    }

    public String getGuildId() {
        return guildId;
    }

    public RoleDto getRole() {
        return role;
    }
}
