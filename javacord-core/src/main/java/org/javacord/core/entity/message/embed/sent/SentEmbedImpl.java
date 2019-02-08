package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.sent.*;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class SentEmbedImpl implements SentEmbed {
    private final String title;
    private final String type;
    private final String description;
    private final String url;
    private final Instant timestamp;
    private final Color color;
    private final SentEmbedAuthor author;
    private final SentEmbedThumbnail thumbnail;
    private final SentEmbedImage image;
    private final SentEmbedFooter footer;
    private final SentEmbedVideo video;
    private final SentEmbedProvider provider;
    private final List<SentEmbedField> fields;

    public SentEmbedImpl(JsonNode data) {
        this.title = data.path("title").asText(null);
        this.type = data.path("type").asText(null);
        this.description = data.path("description").asText(null);
        this.url = data.path("url").asText(null);
        this.timestamp = data.has("timestamp") ? Instant.parse(data.get("timestamp").asText()) : null;
        this.color = data.has("color") ? new Color(data.get("color").asInt()) : null;

        this.author = data.has("author") ? new SentEmbedAuthorImpl(this, data.get("author")) : null;
        this.thumbnail = data.has("thumbnail") ? new SentEmbedThumbnailImpl(this, data.get("thumbnail")) : null;
        this.image = data.has("image") ? new SentEmbedImageImpl(this, data.get("image")) : null;
        this.footer = data.has("footer") ? new SentEmbedFooterImpl(this, data.get("footer")) : null;
        this.video = data.has("video") ? new SentEmbedVideoImpl(this, data.get("video")) : null;
        this.provider = data.has("provider") ? new SentEmbedProviderImpl(this, data.get("provider")) : null;

        this.fields = new ArrayList<>();
        data.path("fields").forEach(node -> fields.add(new SentEmbedFieldImpl(this, node)));
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Optional<String> getTitle() {
        return ofNullable(title);
    }

    @Override
    public Optional<String> getDescription() {
        return ofNullable(description);
    }

    @Override
    public Optional<String> getUrl() {
        return ofNullable(url);
    }

    @Override
    public Optional<Instant> getTimestamp() {
        return ofNullable(timestamp);
    }

    @Override
    public Optional<Color> getColor() {
        return ofNullable(color);
    }

    @Override
    public Optional<SentEmbedFooter> getFooter() {
        return ofNullable(footer);
    }

    @Override
    public Optional<SentEmbedImage> getImage() {
        return ofNullable(image);
    }

    @Override
    public Optional<SentEmbedThumbnail> getThumbnail() {
        return ofNullable(thumbnail);
    }

    @Override
    public Optional<SentEmbedAuthor> getAuthor() {
        return ofNullable(author);
    }

    @Override
    public List<SentEmbedField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public Optional<SentEmbedVideo> getVideo() {
        return ofNullable(video);
    }

    @Override
    public Optional<SentEmbedProvider> getProvider() {
        return ofNullable(provider);
    }
}
