package org.javacord.api.entity.message.embed;

import java.net.URL;

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

}