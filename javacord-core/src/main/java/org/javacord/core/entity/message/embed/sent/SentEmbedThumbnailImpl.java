package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;
import org.javacord.core.entity.message.embed.draft.EmbedDraftThumbnailImpl;

import java.util.Optional;

/**
 * The implementation of {@link SentEmbedThumbnail}.
 */
public class SentEmbedThumbnailImpl implements SentEmbedThumbnail {

    private final SentEmbed parent;
    private final String url;
    private final String proxyUrl;
    private final int height;
    private final int width;

    /**
     * Creates a new embed thumbnail.
     *
     * @param data The json data of the thumbnail.
     */
    public SentEmbedThumbnailImpl(SentEmbed parent, JsonNode data) {
        this.parent = parent;

        url = data.path("url").asText(null);
        proxyUrl = data.path("proxy_url").asText(null);
        height = data.has("height") ? data.get("height").asInt() : -1;
        width = data.has("width") ? data.get("width").asInt() : -1;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.of(url);
    }

    @Override
    public EmbedDraftThumbnail toEmbedDraftThumbnail() {
        return new EmbedDraftThumbnailImpl(parent, this);
    }

    @Override
    public SentEmbed getEmbed() {
        return parent;
    }

    @Override
    public String getProxyUrl() {
        assert proxyUrl != null : "Discord didn't send a proxy URL!";
        return proxyUrl;
    }

    @Override
    public Optional<Integer> getHeight() {
        return height == -1 ? Optional.empty() : Optional.of(height);
    }

    @Override
    public Optional<Integer> getWidth() {
        return width == -1 ? Optional.empty() : Optional.of(width);
    }

}
