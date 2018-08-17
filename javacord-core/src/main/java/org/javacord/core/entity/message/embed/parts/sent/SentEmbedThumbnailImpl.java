package org.javacord.core.entity.message.embed.parts.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedThumbnail;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftThumbnailImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link SentEmbedThumbnail}.
 */
public class SentEmbedThumbnailImpl implements SentEmbedThumbnail {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(SentEmbedThumbnailImpl.class);

    private final String url;
    private final String proxyUrl;
    private final int height;
    private final int width;

    /**
     * Creates a new embed thumbnail.
     *
     * @param data The json data of the thumbnail.
     */
    public SentEmbedThumbnailImpl(JsonNode data) {
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
            logger.warn("Seems like the url of the embed thumbnail is malformed! Please contact the developer!", e);
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
            logger.warn("Seems like the embed thumbnail's proxy url is malformed! Please contact the developer!", e);
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
    public EmbedDraftThumbnail toDraft() {
        return new EmbedDraftThumbnailImpl(
                getUrl().toExternalForm(),
                null
        );
    }

}
