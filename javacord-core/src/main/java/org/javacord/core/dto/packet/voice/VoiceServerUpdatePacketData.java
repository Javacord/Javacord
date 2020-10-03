package org.javacord.core.dto.packet.voice;

import com.fasterxml.jackson.annotation.JsonCreator;

public class VoiceServerUpdatePacketData {

    private final String token;
    private final String guildId;
    private final String endpoint;

    @JsonCreator
    public VoiceServerUpdatePacketData(String token, String guildId, String endpoint) {
        this.token = token;
        this.guildId = guildId;
        this.endpoint = endpoint;
    }

    public String getToken() {
        return token;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
