package org.javacord.api.entity.message.embed;

import org.javacord.api.DiscordApi;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This interface represents an embed thumbnail.
 */
public interface EmbedThumbnail {

    /**
     * Gets the url of the thumbnail.
     *
     * @return The url of the thumbnail.
     */
    URL getUrl();

    /**
     * Gets the proxy url of the thumbnail.
     *
     * @return The proxy url of the thumbnail.
     */
    URL getProxyUrl();

    /**
     * Gets the height of the thumbnail.
     *
     * @return The height of the thumbnail.
     */
    int getHeight();

    /**
     * Gets the width of the thumbnail.
     *
     * @return The width of the thumbnail.
     */
    int getWidth();

    /**
     * Downloads the thumbnail as a {@code BufferedImage}.
     * 
     * @param api The discord api instance used to download the thumbnail.
     * @return The thumbnail as a {@code BufferedImage}.
     */
    CompletableFuture<BufferedImage> downloadAsBufferedImage(DiscordApi api);

    /**
     * Downloads the thumbnail as a byte array.
     *
     * @param api The discord api instance used to download the thumbnail.
     * @return The thumbnail as a byte array.
     */
    CompletableFuture<byte[]> downloadAsByteArray(DiscordApi api);

    /**
     * Downloads the thumbnail as an input stream.
     *
     * @param api The discord api instance used to download the thumbnail.
     * @return The thumbnail as an input stream.
     * @throws IOException If an IO error occurs.
     */
    InputStream downloadAsInputStream(DiscordApi api) throws IOException;

}
