package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class ActivityTimestampsDto {

    @Nullable
    private final Instant start;
    @Nullable
    private final Instant end;

    @JsonCreator
    public ActivityTimestampsDto(@Nullable Instant start, @Nullable Instant end) {
        this.start = start;
        this.end = end;
    }

    public Optional<Instant> getStart() {
        return Optional.ofNullable(start);
    }

    public Optional<Instant> getEnd() {
        return Optional.ofNullable(end);
    }
}
