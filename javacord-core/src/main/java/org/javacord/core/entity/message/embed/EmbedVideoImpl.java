package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedVideo;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link EmbedVideo}.
 */
public class EmbedVideoImpl implements EmbedVideo {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedVideoImpl.class);

    private final String url;
    private final int height;
    private final int width;

    /**
     * Creates a new embed video.
     *
     * @param data The json data of the video.
     */
    public EmbedVideoImpl(JsonNode data) {
        url = data.has("url") ? data.get("url").asText() : null;
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
            logger.warn("Seems like the url of the embed video is malformed! Please contact the developer!", e);
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