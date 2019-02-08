package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.OptionalInt;

/**
 * The implementation of {@link SentEmbedThumbnail}.
 */
public class SentEmbedThumbnailImpl extends SentEmbedMemberImpl<EmbedDraftThumbnail, SentEmbedThumbnail>
        implements SentEmbedThumbnail {

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
        super(parent, EmbedDraftThumbnail.class, SentEmbedThumbnail.class);

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
    public String getProxyUrl() {
        assert proxyUrl != null : "Discord didn't send a proxy URL!";
        return proxyUrl;
    }

    @Override
    public OptionalInt getHeight() {
        return height == -1 ? OptionalInt.empty() : OptionalInt.of(height);
    }

    @Override
    public OptionalInt getWidth() {
        return width == -1 ? OptionalInt.empty() : OptionalInt.of(width);
    }

}
