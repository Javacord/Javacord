package org.javacord.api.entity.message.embed;

import java.net.URL;

/**
 * This interface represents an embed video.
 */
public interface EmbedVideo {

    /**
     * Gets the url of the video.
     *
     * @return The url of the video.
     */
    URL getUrl();

    /**
     * Gets the height of the video.
     *
     * @return The height of the video.
     */
    int getHeight();

    /**
     * Gets the width of the video.
     *
     * @return The width of the video.
     */
    int getWidth();

}