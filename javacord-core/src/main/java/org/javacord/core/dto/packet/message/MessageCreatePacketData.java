package org.javacord.core.dto.packet.message;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.MessageDto;

public class MessageCreatePacketData {

    @JsonUnwrapped
    private final MessageDto message;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private MessageCreatePacketData() {
        this(null);
    }

    public MessageCreatePacketData(MessageDto message) {
        this.message = message;
    }

    public MessageDto getMessage() {
        return message;
    }
}
