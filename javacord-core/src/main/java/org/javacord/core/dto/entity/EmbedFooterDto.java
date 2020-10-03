package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmbedFooterDto {

    private final String text;
    @Nullable
    private final String iconUrl;
    @Nullable
    private final String proxyIconUrl;

    @JsonCreator
    public EmbedFooterDto(String text, @Nullable String iconUrl, @Nullable String proxyIconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
        this.proxyIconUrl = proxyIconUrl;
    }

    public String getText() {
        return text;
    }

    public Optional<String> getIconUrl() {
        return Optional.ofNullable(iconUrl);
    }

    public Optional<String> getProxyIconUrl() {
        return Optional.ofNullable(proxyIconUrl);
    }
}
