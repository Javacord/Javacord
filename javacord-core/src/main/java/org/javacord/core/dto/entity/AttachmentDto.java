package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AttachmentDto {

    private final String id;
    private final String filename;
    private final int size;
    private final String url;
    private final String proxyUrl;
    @Nullable
    private final Integer height;
    @Nullable
    private final Integer width;

    @JsonCreator
    public AttachmentDto(String id, String filename, int size, String url, String proxyUrl, @Nullable Integer height, @Nullable Integer width) {
        this.id = id;
        this.filename = filename;
        this.size = size;
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.height = height;
        this.width = width;
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }
}
