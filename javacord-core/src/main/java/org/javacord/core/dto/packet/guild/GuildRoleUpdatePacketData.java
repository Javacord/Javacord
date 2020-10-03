package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.RoleDto;

public class GuildRoleUpdatePacketData {

    private final String guildId;
    private final RoleDto role;

    @JsonCreator
    public GuildRoleUpdatePacketData(String guildId, RoleDto role) {
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
