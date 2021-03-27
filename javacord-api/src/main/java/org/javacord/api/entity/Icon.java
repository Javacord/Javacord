package org.javacord.api.entity;

import org.javacord.api.entity.message.Messageable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a discord icon, for example a server icon or a user avatar.
 */
public interface Icon {

    /**
     * Gets the url of the icon.
     *
     * @return The url of the icon.
     */
    URL getUrl();

    /**
     * Checks if the icon is animated.
     *
     * @return Whether the icon is animated or not.
     */
    default boolean isAnimated() {
        return getUrl().getFile().endsWith(".gif");
    }

    /**
     * Gets the icon as byte array.
     *
     * @return The icon as byte array.
     */
    CompletableFuture<byte[]> asByteArray();

    /**
     * Gets the input stream for the icon.
     * This can be used for {@link Messageable#sendMessage(InputStream, String)}
     *
     * @return The input stream for the icon.
     * @throws IOException If an IO error occurs.
     */
    InputStream asInputStream() throws IOException;

    /**
     * Gets the icon as {@link BufferedImage}.
     *
     * @return The icon as BufferedImage.
     */
    CompletableFuture<BufferedImage> asBufferedImage();

}
