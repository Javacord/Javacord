package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageActivityDto {

    private final Integer type;
    @Nullable
    private final String partyId;

    @JsonCreator
    public MessageActivityDto(Integer type, @Nullable String partyId) {
        this.type = type;
        this.partyId = partyId;
    }

    public Integer getType() {
        return type;
    }

    public Optional<String> getPartyId() {
        return Optional.ofNullable(partyId);
    }
}
