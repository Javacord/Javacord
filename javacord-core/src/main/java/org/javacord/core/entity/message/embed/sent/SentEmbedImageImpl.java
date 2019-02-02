package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.OptionalInt;

/**
 * The implementation of {@link SentEmbedImage}.
 */
public class SentEmbedImageImpl extends SentEmbedMemberImpl<EmbedDraftImage, SentEmbedImage> implements SentEmbedImage {

    private final URL url;
    private final URL proxyUrl;
    private final int height;
    private final int width;

    /**
     * Creates a new embed image.
     *
     * @param data The json data of the image.
     */
    public SentEmbedImageImpl(SentEmbed parent, JsonNode data) {
        super(parent, EmbedDraftImage.class, SentEmbedImage.class);

        try {
            url = data.has("url") ? new URL(data.get("url").asText()) : null;
            proxyUrl = data.has("proxy_url") ? new URL(data.get("proxy_url").asText()) : null;
        } catch (MalformedURLException e) {
            /*
            If any URL cannot be parsed, we have reaced an unreachable state, as the URL fields are
            OPTIONAL but not NULLABLE. We can assert that URLs coming from Discord are always valid.
            */
            throw new AssertionError("The URL recieved from discord is invalid!", e);
        }
        height = data.path("height").asInt(-1);
        width = data.path("width").asInt(-1);
    }

    @Override
    public Optional<URL> getUrl() {
        return Optional.of(url);
    }

    @Override
    public URL getProxyUrl() {
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