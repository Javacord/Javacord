package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import java.util.OptionalInt;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;

/**
 * The implementation of {@link SentEmbedImage}.
 */
public class SentEmbedImageImpl extends SentEmbedMemberImpl<EmbedDraftImage, SentEmbedImage> implements SentEmbedImage {

    private final String url;
    private final String proxyUrl;
    private final int height;
    private final int width;

    /**
     * Creates a new embed image.
     *
     * @param data The json data of the image.
     */
    public SentEmbedImageImpl(SentEmbed parent, JsonNode data) {
        super(parent, EmbedDraftImage.class, SentEmbedImage.class);

        url = data.path("url").asText(null);
        proxyUrl = data.path("proxy_url").asText(null);
        height = data.path("height").asInt(-1);
        width = data.path("width").asInt(-1);
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