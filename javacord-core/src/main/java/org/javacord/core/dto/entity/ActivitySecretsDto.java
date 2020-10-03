package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ActivitySecretsDto {

    @Nullable
    private final String join;
    @Nullable
    private final String spectate;
    @Nullable
    private final String match;

    @JsonCreator
    public ActivitySecretsDto(@Nullable String join, @Nullable String spectate, @Nullable String match) {
        this.join = join;
        this.spectate = spectate;
        this.match = match;
    }

    public Optional<String> getJoin() {
        return Optional.ofNullable(join);
    }

    public Optional<String> getSpectate() {
        return Optional.ofNullable(spectate);
    }

    public Optional<String> getMatch() {
        return Optional.ofNullable(match);
    }
}
