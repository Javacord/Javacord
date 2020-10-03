package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmbedProviderDto {

    @Nullable
    private final String name;
    @Nullable
    private final String url;

    @JsonCreator
    public EmbedProviderDto(@Nullable String name, @Nullable String url) {
        this.name = name;
        this.url = url;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }
}
