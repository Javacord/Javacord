package org.javacord.entity.message.embed.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.entity.message.embed.EmbedVideo;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.entity.message.embed.EmbedVideo;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link EmbedVideo}.
 */
public class ImplEmbedVideo implements EmbedVideo {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbedVideo.class);

    private String url;
    private int height;
    private int width;

    /**
     * Creates a new embed video.
     *
     * @param data The json data of the video.
     */
    public ImplEmbedVideo(JsonNode data) {
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