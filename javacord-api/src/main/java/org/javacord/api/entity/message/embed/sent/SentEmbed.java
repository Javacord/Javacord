package org.javacord.api.entity.message.embed.sent;

import java.util.List;
import java.util.Optional;
import org.javacord.api.entity.message.embed.BaseEmbed;

public interface SentEmbed extends BaseEmbed {

    /**
     * Gets the type of the embed.
     * (always "rich" for webhook embeds)
     *
     * @return The type of the embed.
     */
    String getType();

    /**
     * Gets the video of the embed.
     *
     * @return The video of the embed.
     */
    Optional<SentEmbedVideo> getVideo();

    /**
     * Gets the provider of the embed.
     *
     * @return The provider of the embed.
     */
    Optional<SentEmbedProvider> getProvider();

    @Override
    Optional<SentEmbedFooter> getFooter();

    @Override
    Optional<SentEmbedImage> getImage();

    @Override
    Optional<SentEmbedThumbnail> getThumbnail();

    @Override
    Optional<SentEmbedAuthor> getAuthor();

    @Override
    List<SentEmbedField> getFields();
}
