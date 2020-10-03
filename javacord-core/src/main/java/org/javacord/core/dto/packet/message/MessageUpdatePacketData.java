package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.PartialMessageDto;

public class MessageUpdatePacketData {

    @JsonUnwrapped
    private PartialMessageDto message;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private MessageUpdatePacketData() {
        this(null);
    }

    public MessageUpdatePacketData(PartialMessageDto message) {
        this.message = message;
    }

    public PartialMessageDto getMessage() {
        return message;
    }
}
