package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClientStatusDto {

    @Nullable
    private final String desktop;
    @Nullable
    private final String mobile;
    @Nullable
    private final String web;

    @JsonCreator
    public ClientStatusDto(@Nullable String desktop, @Nullable String mobile, @Nullable String web) {
        this.desktop = desktop;
        this.mobile = mobile;
        this.web = web;
    }

    public Optional<String> getDesktop() {
        return Optional.ofNullable(desktop);
    }

    public Optional<String> getMobile() {
        return Optional.ofNullable(mobile);
    }

    public Optional<String> getWeb() {
        return Optional.ofNullable(web);
    }
}
