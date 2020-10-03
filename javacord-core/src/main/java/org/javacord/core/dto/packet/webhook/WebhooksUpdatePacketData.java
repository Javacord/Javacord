package org.javacord.core.dto.packet.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;

public class WebhooksUpdatePacketData {

    private final String guildId;
    private final String channelId;

    @JsonCreator
    public WebhooksUpdatePacketData(String guildId, String channelId) {
        this.guildId = guildId;
        this.channelId = channelId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }
}
