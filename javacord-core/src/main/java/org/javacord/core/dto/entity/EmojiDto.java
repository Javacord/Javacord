package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmojiDto {

    @Nullable
    private final String id;
    @Nullable
    private final String name;
    @Nullable
    @JsonProperty("roles")
    private final String roleIds;
    @Nullable
    private final UserDto user;
    @Nullable
    private final Boolean requireColons;
    @Nullable
    private final Boolean managed;
    @Nullable
    private final Boolean animated;
    @Nullable
    private final Boolean available;

    @JsonCreator
    public EmojiDto(@Nullable String id, @Nullable String name, @Nullable String roleIds, @Nullable UserDto user, @Nullable Boolean requireColons, @Nullable Boolean managed, @Nullable Boolean animated, @Nullable Boolean available) {
        this.id = id;
        this.name = name;
        this.roleIds = roleIds;
        this.user = user;
        this.requireColons = requireColons;
        this.managed = managed;
        this.animated = animated;
        this.available = available;
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getRoleIds() {
        return Optional.ofNullable(roleIds);
    }

    public Optional<UserDto> getUser() {
        return Optional.ofNullable(user);
    }

    public Optional<Boolean> getRequireColons() {
        return Optional.ofNullable(requireColons);
    }

    public Optional<Boolean> getManaged() {
        return Optional.ofNullable(managed);
    }

    public Optional<Boolean> getAnimated() {
        return Optional.ofNullable(animated);
    }

    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }
}
