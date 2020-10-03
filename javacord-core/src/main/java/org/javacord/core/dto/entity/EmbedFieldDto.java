package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EmbedFieldDto {

    private final String name;
    private final String value;
    @Nullable
    private final Boolean inline;

    @JsonCreator
    public EmbedFieldDto(String name, String value, @Nullable Boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Optional<Boolean> getInline() {
        return Optional.ofNullable(inline);
    }
}
