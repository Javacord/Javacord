package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;

import java.util.Optional;

public interface SentEmbedThumbnail extends BaseEmbedThumbnail {

    @Override
    SentEmbed getEmbed();

    /**
     * Gets the proxy url of the image.
     *
     * @return The proxy url of the image.
     */
    String getProxyUrl();

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
