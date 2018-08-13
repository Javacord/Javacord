package org.javacord.api.entity.message.embed.parts.sent;

import java.net.URL;

public interface SentEmbedVideo {

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
