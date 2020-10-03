package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ActivityEmojiDto {

    private final String name;
    @Nullable
    private final String id;
    @Nullable
    private final Boolean animated;

    @JsonCreator
    public ActivityEmojiDto(String name, @Nullable String id, @Nullable Boolean animated) {
        this.name = name;
        this.id = id;
        this.animated = animated;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Boolean> getAnimated() {
        return Optional.ofNullable(animated);
    }
}
