package org.javacord.core.dto.packet.presence;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.PresenceDto;

public class PresenceUpdatePacketData {

    @JsonUnwrapped
    private final PresenceDto presence;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private PresenceUpdatePacketData() {
        this(null);
    }

    public PresenceUpdatePacketData(PresenceDto presence) {
        this.presence = presence;
    }

    public PresenceDto getPresence() {
        return presence;
    }
}
