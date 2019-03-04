package org.javacord.api.entity.message.embed;

import java.util.Optional;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;
import org.javacord.api.util.Specializable;

/**
 * Basic representation of an embed's image.
 */
public interface BaseEmbedImage extends Specializable<BaseEmbedImage> {

    /**
     * Gets the embed that this image is part of.
     *
     * @return The parent embed.
     */
    BaseEmbed getEmbed();

    /**
     * Gets the url of the image.
     *
     * @return The url of the image.
     */
    Optional<String> getUrl();

    /**
     * Gets this image as a {@code EmbedDraftImage}.
     * If this instance already is an {@code EmbedDraftImage}, this method will return the cast instance.
     *
     * @return A {@code EmbedDraftImage}.
     */
    EmbedDraftImage toEmbedDraftImage();

    /**
     * Gets this image as a {@code SentEmbedImage}.
     *
     * @return A {@code SentEmbedImage}.
     */
    default Optional<SentEmbedImage> asSentEmbedImage() {
        return as(SentEmbedImage.class);
    }

}
