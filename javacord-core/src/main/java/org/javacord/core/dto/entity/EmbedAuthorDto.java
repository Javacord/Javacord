package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmbedAuthorDto {

    @Nullable
    private final String name;
    @Nullable
    private final String url;
    @Nullable
    private final String iconUrl;
    @Nullable
    private final String proxyIconUrl;

    @JsonCreator
    public EmbedAuthorDto(@Nullable String name, @Nullable String url, @Nullable String iconUrl, @Nullable String proxyIconUrl) {
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
        this.proxyIconUrl = proxyIconUrl;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    public Optional<String> getIconUrl() {
        return Optional.ofNullable(iconUrl);
    }

    public Optional<String> getProxyIconUrl() {
        return Optional.ofNullable(proxyIconUrl);
    }
}
