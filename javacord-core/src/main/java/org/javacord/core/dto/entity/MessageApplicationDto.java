package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageApplicationDto {

    private final String id;
    @Nullable
    private final String coverImage;
    private final String description;
    @Nullable
    private final String icon;
    private final String name;

    @JsonCreator
    public MessageApplicationDto(String id, @Nullable String coverImage, String description, @Nullable String icon, String name) {
        this.id = id;
        this.coverImage = coverImage;
        this.description = description;
        this.icon = icon;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Optional<String> getCoverImage() {
        return Optional.ofNullable(coverImage);
    }

    public String getDescription() {
        return description;
    }

    public Optional<String> getIcon() {
        return Optional.ofNullable(icon);
    }

    public String getName() {
        return name;
    }
}
