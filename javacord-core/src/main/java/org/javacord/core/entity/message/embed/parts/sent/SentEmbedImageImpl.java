package org.javacord.core.entity.message.embed.parts.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedImage;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftImageImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link SentEmbedImage}.
 */
public class SentEmbedImageImpl implements SentEmbedImage {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(SentEmbedImageImpl.class);

    private final String url;
    private final String proxyUrl;
    private final int height;
    private final int width;

    /**
     * Creates a new embed image.
     *
     * @param data The json data of the image.
     */
    public SentEmbedImageImpl(JsonNode data) {
        url = data.has("url") ? data.get("url").asText() : null;
        proxyUrl = data.has("proxy_url") ? data.get("proxy_url").asText() : null;
        height = data.has("height") ? data.get("height").asInt() : -1;
        width = data.has("width") ? data.get("width").asInt() : -1;
    }

    @Override
    public URL getUrl() {
        if (url == null) {
            return null;
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed image is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public URL getProxyUrl() {
        if (proxyUrl == null) {
            return null;
        }
        try {
            return new URL(proxyUrl);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the proxy url of the embed image is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public EmbedDraftImage toDraft() {
        return new EmbedDraftImageImpl(
                getUrl().toExternalForm(),
                null
        );
    }

}