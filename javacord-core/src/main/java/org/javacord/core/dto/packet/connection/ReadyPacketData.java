package org.javacord.core.dto.packet.connection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.javacord.core.dto.entity.UnavailableGuildDto;
import org.javacord.core.dto.entity.UserDto;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ReadyPacketData {

    @JsonProperty("v")
    private final int version;
    private final UserDto user;
    private final UnavailableGuildDto[] guilds;
    private final String sessionId;
    @Nullable
    private final Integer[] shard;

    @JsonCreator
    public ReadyPacketData(int version, UserDto user, UnavailableGuildDto[] guilds, String sessionId, @Nullable Integer[] shard) {
        this.version = version;
        this.user = user;
        this.guilds = guilds;
        this.sessionId = sessionId;
        this.shard = shard;
    }

    public int getVersion() {
        return version;
    }

    public UserDto getUser() {
        return user;
    }

    public UnavailableGuildDto[] getGuilds() {
        return guilds;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Optional<Integer[]> getShard() {
        return Optional.ofNullable(shard);
    }
}
