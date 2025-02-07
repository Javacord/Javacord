package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedImage;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link EmbedImage}.
 */
public class EmbedImageImpl implements EmbedImage {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedImageImpl.class);

    private final String url;
    private final String proxyUrl;
    private final Integer height;
    private final Integer width;

    /**
     * Creates a new embed image.
     *
     * @param data The json data of the image.
     */
    public EmbedImageImpl(JsonNode data) {
        url = data.has("url") ? data.get("url").asText() : null;
        proxyUrl = data.has("proxy_url") ? data.get("proxy_url").asText() : null;
        height = data.hasNonNull("height") ? data.get("height").asInt() : null;
        width = data.hasNonNull("width") ? data.get("width").asInt() : null;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed image is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Optional<URL> getProxyUrl() {
        if (proxyUrl == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(proxyUrl));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the proxy url of the embed image is malformed! Please contact the developer!", e);
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

    @Override
    public CompletableFuture<BufferedImage> asBufferedImage(DiscordApi api) {
        return new FileContainer(getUrl()).asBufferedImage(api);
    }

    @Override
    public CompletableFuture<byte[]> asByteArray(DiscordApi api) {
        return new FileContainer(getUrl()).asByteArray(api);
    }

    @Override
    public InputStream asInputStream(DiscordApi api) throws IOException {
        return new FileContainer(getUrl()).asInputStream(api);
    }

}