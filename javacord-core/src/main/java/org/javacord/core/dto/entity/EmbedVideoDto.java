package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmbedVideoDto {

    @Nullable
    private final String url;
    @Nullable
    private final Integer height;
    @Nullable
    private final Integer width;

    @JsonCreator
    public EmbedVideoDto(@Nullable String url, @Nullable Integer height, @Nullable Integer width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }
}
