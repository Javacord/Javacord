package org.javacord.api.entity.message.embed;

import java.util.Optional;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;
import org.javacord.api.util.Specializable;

/**
 * Basic representation of an embed's thumbnail.
 */
public interface BaseEmbedThumbnail extends Specializable<BaseEmbedThumbnail> {

    /**
     * Gets the embed that this thumbnail is part of.
     *
     * @return The parent embed.
     */
    BaseEmbed getEmbed();

    /**
     * Gets the url of the thumbnail.
     *
     * @return The url of the thumbnail.
     */
    Optional<String> getUrl();

    /**
     * Gets this thumbnail as a {@code EmbedDraftThumbnail}.
     * If this instance already is an {@code EmbedDraftThumbnail}, this method will return the cast instance.
     *
     * @return A {@code EmbedDraftThumbnail}.
     */
    EmbedDraftThumbnail toEmbedDraftThumbnail();

    /**
     * Gets this thumbnail as a {@code SentEmbedThumbnail}.
     *
     * @return A {@code SentEmbedThumbnail}.
     */
    default Optional<SentEmbedThumbnail> asSentEmbedThumbnail() {
        return as(SentEmbedThumbnail.class);
    }

}
