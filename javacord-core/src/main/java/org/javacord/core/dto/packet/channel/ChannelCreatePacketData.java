package org.javacord.core.dto.packet.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.ChannelDto;

public class ChannelCreatePacketData {

    @JsonUnwrapped
    private final ChannelDto channel;

    @JsonCreator
    public ChannelCreatePacketData(ChannelDto channel) {
        this.channel = channel;
    }

    public ChannelDto getChannel() {
        return channel;
    }
}
