package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UnavailableGuildDto {

    private final String id;
    private final boolean unavailable;

    @JsonCreator
    public UnavailableGuildDto(String id, boolean unavailable) {
        this.id = id;
        this.unavailable = unavailable;
    }

    public String getId() {
        return id;
    }

    public boolean isUnavailable() {
        return unavailable;
    }
}
