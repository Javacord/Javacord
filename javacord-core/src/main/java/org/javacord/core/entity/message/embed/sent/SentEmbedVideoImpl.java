package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedVideo;

import java.util.Optional;

/**
 * The implementation of {@link SentEmbedVideo}.
 */
public class SentEmbedVideoImpl implements SentEmbedVideo {

    private final SentEmbed parent;
    private final String url;
    private final int height;
    private final int width;

    /**
     * Creates a new embed video.
     *
     * @param data The json data of the video.
     */
    public SentEmbedVideoImpl(SentEmbed parent, JsonNode data) {
        this.parent = parent;

        url = data.path("url").asText(null);
        height = data.has("height") ? data.get("height").asInt() : -1;
        width = data.has("width") ? data.get("width").asInt() : -1;
    }

    @Override
    public String getUrl() {
        assert url != null : "Discord didn't send an URL!";
        return url;
    }

    @Override
    public Optional<Integer> getHeight() {
        return height == -1 ? Optional.empty() : Optional.of(height);
    }

    @Override
    public Optional<Integer> getWidth() {
        return width == -1 ? Optional.empty() : Optional.of(width);
    }

    @Override
    public Optional<SentEmbed> getEmbed() {
        return Optional.ofNullable(parent);
    }
}