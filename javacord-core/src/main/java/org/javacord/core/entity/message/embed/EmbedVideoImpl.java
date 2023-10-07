package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedVideo;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link EmbedVideo}.
 */
public class EmbedVideoImpl implements EmbedVideo {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedVideoImpl.class);

    private final String url;
    private final Integer height;
    private final Integer width;

    /**
     * Creates a new embed video.
     *
     * @param data The json data of the video.
     */
    public EmbedVideoImpl(JsonNode data) {
        url = data.has("url") ? data.get("url").asText() : null;
        height = data.has("height") ? data.get("height").asInt() : null;
        width = data.has("width") ? data.get("width").asInt() : null;
    }

    @Override
    public Optional<URL> getUrl() {
        if (url == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed video is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }

}