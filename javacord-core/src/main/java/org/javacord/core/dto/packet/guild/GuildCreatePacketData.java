package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.GuildDto;

public class GuildCreatePacketData {

    @JsonUnwrapped
    private final GuildDto guild;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private GuildCreatePacketData() {
        this(null);
    }

    public GuildCreatePacketData(GuildDto guild) {
        this.guild = guild;
    }

    public GuildDto getGuild() {
        return guild;
    }
}
