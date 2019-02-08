package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.OptionalInt;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedVideo;

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
    public OptionalInt getHeight() {
        return height == -1 ? OptionalInt.empty() : OptionalInt.of(height);
    }

    @Override
    public OptionalInt getWidth() {
        return width == -1 ? OptionalInt.empty() : OptionalInt.of(width);
    }

    @Override
    public Optional<SentEmbed> getEmbed() {
        return Optional.ofNullable(parent);
    }
}