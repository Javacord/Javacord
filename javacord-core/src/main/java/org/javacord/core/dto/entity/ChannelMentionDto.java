package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChannelMentionDto {

    private final String id;
    private final String guildId;
    private final int type;
    private final String name;

    @JsonCreator
    public ChannelMentionDto(String id, String guildId, int type, String name) {
        this.id = id;
        this.guildId = guildId;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getGuildId() {
        return guildId;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
