package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ActivityAssetsDto {

    @Nullable
    private final String largeImage;
    @Nullable
    private final String largeText;
    @Nullable
    private final String smallImage;
    @Nullable
    private final String smallText;

    @JsonCreator
    public ActivityAssetsDto(@Nullable String largeImage, @Nullable String largeText, @Nullable String smallImage, @Nullable String smallText) {
        this.largeImage = largeImage;
        this.largeText = largeText;
        this.smallImage = smallImage;
        this.smallText = smallText;
    }

    public Optional<String> getLargeImage() {
        return Optional.ofNullable(largeImage);
    }

    public Optional<String> getLargeText() {
        return Optional.ofNullable(largeText);
    }

    public Optional<String> getSmallImage() {
        return Optional.ofNullable(smallImage);
    }

    public Optional<String> getSmallText() {
        return Optional.ofNullable(smallText);
    }
}
