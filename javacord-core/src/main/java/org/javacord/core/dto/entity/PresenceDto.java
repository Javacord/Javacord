package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PresenceDto {

    private final PartialUserDto user;
    private final String guildId;
    private final String status;
    private final ActivityDto[] activities;
    private final ClientStatusDto clientStatus;

    @JsonCreator
    public PresenceDto(PartialUserDto user, String guildId, String status, ActivityDto[] activities, ClientStatusDto clientStatus) {
        this.user = user;
        this.guildId = guildId;
        this.status = status;
        this.activities = activities;
        this.clientStatus = clientStatus;
    }

    public PartialUserDto getUser() {
        return user;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getStatus() {
        return status;
    }

    public ActivityDto[] getActivities() {
        return activities;
    }

    public ClientStatusDto getClientStatus() {
        return clientStatus;
    }
}
