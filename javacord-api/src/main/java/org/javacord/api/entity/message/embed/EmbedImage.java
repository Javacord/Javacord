package org.javacord.api.entity.message.embed;

import org.javacord.api.DiscordApi;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This interface represents an embed image.
 */
public interface EmbedImage {

    /**
     * Gets the url of the image.
     *
     * @return The url of the image.
     */
    URL getUrl();

    /**
     * Gets the proxy url of the image.
     *
     * @return The proxy url of the image.
     */
    Optional<URL> getProxyUrl();

    /**
     * Gets the height of the image.
     *
     * @return The height of the image.
     */
    Optional<Integer> getHeight();

    /**
     * Gets the width of the image.
     *
     * @return The width of the image.
     */
    Optional<Integer> getWidth();

    /**
     * Downloads the image as a {@code BufferedImage}.
     *
     * @param api The discord api instance used to download the image.
     * @return The thumbnail as a {@code BufferedImage}.
     */
    CompletableFuture<BufferedImage> asBufferedImage(DiscordApi api);

    /**
     * Downloads the image as a byte array.
     *
     * @param api The discord api instance used to download the image.
     * @return The thumbnail as a byte array.
     */
    CompletableFuture<byte[]> asByteArray(DiscordApi api);

    /**
     * Downloads the image as an input stream.
     *
     * @param api The discord api instance used to download the image.
     * @return The thumbnail as an input stream.
     * @throws IOException If an IO error occurs.
     */
    InputStream asInputStream(DiscordApi api) throws IOException;

}