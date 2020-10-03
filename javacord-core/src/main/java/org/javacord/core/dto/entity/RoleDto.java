package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class RoleDto {

    private final String id;
    private final String name;
    private final int color;
    private final boolean hoist;
    private final int position;
    private final String permissions;
    private final boolean managed;
    private final boolean mentionable;

    @JsonCreator
    public RoleDto(String id, String name, int color, boolean hoist, int position, String permissions, boolean managed, boolean mentionable) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.hoist = hoist;
        this.position = position;
        this.permissions = permissions;
        this.managed = managed;
        this.mentionable = mentionable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public boolean isHoist() {
        return hoist;
    }

    public int getPosition() {
        return position;
    }

    public String getPermissions() {
        return permissions;
    }

    public boolean isManaged() {
        return managed;
    }

    public boolean isMentionable() {
        return mentionable;
    }
}
