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
    private final URL url;
    private final int height;
    private final int width;

    /**
     * Creates a new embed video.
     *
     * @param data The json data of the video.
     */
    public SentEmbedVideoImpl(SentEmbed parent, JsonNode data) {
        this.parent = parent;

        try {
            url = data.has("url") ? new URL(data.get("url").asText()) : null;
        } catch (MalformedURLException e) {
            /*
            If any URL cannot be parsed, we have reached an unreachable state, as the URL fields are
            OPTIONAL but not NULLABLE. We can assert that URLs coming from Discord are always valid.
            */
            throw new AssertionError("The URL recieved from discord is invalid!", e);
        }
        height = data.has("height") ? data.get("height").asInt() : -1;
        width = data.has("width") ? data.get("width").asInt() : -1;
    }

    @Override
    public URL getUrl() {
        assert url != null : "Discord didn't send a URL!";
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