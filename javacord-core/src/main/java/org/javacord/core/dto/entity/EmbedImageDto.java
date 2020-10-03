package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmbedImageDto {

    @Nullable
    private final String url;
    @Nullable
    private final String proxyUrl;
    @Nullable
    private final Integer height;
    @Nullable
    private final Integer width;

    @JsonCreator
    public EmbedImageDto(@Nullable String url, @Nullable String proxyUrl, @Nullable Integer height, @Nullable Integer width) {
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.height = height;
        this.width = width;
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    public Optional<String> getProxyUrl() {
        return Optional.ofNullable(proxyUrl);
    }

    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }
}
