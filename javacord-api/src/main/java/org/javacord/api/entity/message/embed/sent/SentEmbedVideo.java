package org.javacord.api.entity.message.embed.sent;


import java.util.Optional;

public interface SentEmbedVideo {

    /**
     * Gets the embed that this video is part of.
     *
     * @return The parent embed.
     */
    Optional<SentEmbed> getEmbed();

    /**
     * Gets the url of the video.
     *
     * @return The url of the video.
     */
    String getUrl();

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

}
