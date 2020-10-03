package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class EmbedDto {

    @Nullable
    private final String title;
    @Nullable
    private final String type;
    @Nullable
    private final String description;
    @Nullable
    private final String url;
    @Nullable
    private final Instant timestamp;
    @Nullable
    private final Integer color;
    @Nullable
    private final EmbedFooterDto footer;
    @Nullable
    private final EmbedImageDto image;
    @Nullable
    private final EmbedThumbnailDto thumbnail;
    @Nullable
    private final EmbedVideoDto video;
    @Nullable
    private final EmbedProviderDto provider;
    @Nullable
    private final EmbedAuthorDto author;
    @Nullable
    private final EmbedFieldDto[] fields;

    @JsonCreator
    public EmbedDto(@Nullable String title, @Nullable String type, @Nullable String description, @Nullable String url, @Nullable Instant timestamp, @Nullable Integer color, @Nullable EmbedFooterDto footer, @Nullable EmbedImageDto image, @Nullable EmbedThumbnailDto thumbnail, @Nullable EmbedVideoDto video, @Nullable EmbedProviderDto provider, @Nullable EmbedAuthorDto author, @Nullable EmbedFieldDto[] fields) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.video = video;
        this.provider = provider;
        this.author = author;
        this.fields = fields;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    public Optional<Instant> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public Optional<Integer> getColor() {
        return Optional.ofNullable(color);
    }

    public Optional<EmbedFooterDto> getFooter() {
        return Optional.ofNullable(footer);
    }

    public Optional<EmbedImageDto> getImage() {
        return Optional.ofNullable(image);
    }

    public Optional<EmbedThumbnailDto> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<EmbedVideoDto> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<EmbedProviderDto> getProvider() {
        return Optional.ofNullable(provider);
    }

    public Optional<EmbedAuthorDto> getAuthor() {
        return Optional.ofNullable(author);
    }

    public Optional<EmbedFieldDto[]> getFields() {
        return Optional.ofNullable(fields);
    }
}
