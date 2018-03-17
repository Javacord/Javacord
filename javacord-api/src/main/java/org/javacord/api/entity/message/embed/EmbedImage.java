package org.javacord.api.entity.message.embed;

import java.net.URL;

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
    URL getProxyUrl();

    /**
     * Gets the height of the image.
     *
     * @return The height of the image.
     */
    int getHeight();

    /**
     * Gets the width of the image.
     *
     * @return The width of the image.
     */
    int getWidth();

}