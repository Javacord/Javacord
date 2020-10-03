package org.javacord.core.dto.packet.voice;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.VoiceStateDto;

public class VoiceStateUpdatePacketData {

    @JsonUnwrapped
    private final VoiceStateDto voiceState;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private VoiceStateUpdatePacketData() {
        this(null);
    }

    public VoiceStateUpdatePacketData(VoiceStateDto voiceState) {
        this.voiceState = voiceState;
    }

    public VoiceStateDto getVoiceState() {
        return voiceState;
    }
}
