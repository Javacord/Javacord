package de.btobastian.javacord.entities.message.embed.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.entities.message.embed.EmbedImage;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link EmbedImage}.
 */
public class ImplEmbedImage implements EmbedImage {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbedImage.class);

    private String url;
    private String proxyUrl;
    private int height;
    private int width;

    /**
     * Creates a new embed image.
     *
     * @param data The json data of the image.
     */
    public ImplEmbedImage(JsonNode data) {
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

}