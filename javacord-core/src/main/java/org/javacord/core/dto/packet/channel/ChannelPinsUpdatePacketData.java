package org.javacord.core.dto.packet.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class ChannelPinsUpdatePacketData {

    @Nullable
    private final String guildId;

    private final String channelId;

    @Nullable
    private final Instant lastPinTimestamp;

    @JsonCreator
    public ChannelPinsUpdatePacketData(@Nullable String guildId, String channelId, @Nullable Instant lastPinTimestamp) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.lastPinTimestamp = lastPinTimestamp;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<Instant> getLastPinTimestamp() {
        return Optional.ofNullable(lastPinTimestamp);
    }
}
