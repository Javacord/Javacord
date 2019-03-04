package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.Nameable;

public interface SentEmbedProvider extends Nameable {

    /**
     * Gets the embed that this video is part of.
     *
     * @return The parent embed.
     */
    SentEmbed getEmbed();

    /**
     * Gets the url of the provider.
     *
     * @return The url of the provider.
     */
    String getUrl();

}
