package org.javacord.core.dto.packet.presence;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.UserDto;

public class UserUpdatePacketData {

    @JsonUnwrapped
    private final UserDto user;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private UserUpdatePacketData() {
        this(null);
    }

    public UserUpdatePacketData(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}
