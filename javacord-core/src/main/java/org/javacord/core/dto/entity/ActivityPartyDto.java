package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ActivityPartyDto {

    @Nullable
    private final String id;
    @Nullable
    private final int[] size;

    @JsonCreator
    public ActivityPartyDto(@Nullable String id, @Nullable int[] size) {
        this.id = id;
        this.size = size;
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<int[]> getSize() {
        return Optional.ofNullable(size);
    }
}
