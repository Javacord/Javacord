package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OverwriteDto {

    private final String id;
    private final int type;
    private final String allow;
    private final String deny;

    @JsonCreator
    public OverwriteDto(String id, int type, String allow, String deny) {
        this.id = id;
        this.type = type;
        this.allow = allow;
        this.deny = deny;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getAllow() {
        return allow;
    }

    public String getDeny() {
        return deny;
    }
}
