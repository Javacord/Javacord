package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.UnavailableGuildDto;

public class GuildDeletePacketData {

    @JsonUnwrapped
    private final UnavailableGuildDto unavailableGuild;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private GuildDeletePacketData() {
        this(null);
    }

    public GuildDeletePacketData(UnavailableGuildDto unavailableGuild) {
        this.unavailableGuild = unavailableGuild;
    }

    public UnavailableGuildDto getUnavailableGuild() {
        return unavailableGuild;
    }
}
