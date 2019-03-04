package org.javacord.api.entity.message.embed;

import java.util.Optional;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;
import org.javacord.api.util.Specializable;

/**
 * Basic representation of an embed's author.
 */
public interface BaseEmbedAuthor extends Nameable, Specializable<BaseEmbedAuthor> {

    /**
     * Gets the embed that this author is part of.
     *
     * @return The parent embed.
     */
    BaseEmbed getEmbed();

    /**
     * Gets the url of the author.
     *
     * @return The url of the author.
     */
    Optional<String> getUrl();

    /**
     * Gets the url of the author icon.
     *
     * @return The url of the author icon.
     */
    Optional<String> getIconUrl();

    /**
     * Gets this author as a {@code EmbedDraftAuthor}.
     * If this instance already is an {@code EmbedDraftAuthor}, this method will return the cast instance.
     *
     * @return A {@code EmbedDraftAuthor}.
     */
    EmbedDraftAuthor toEmbedDraftAuthor();

    /**
     * Gets this author as a {@code SentEmbedAuthor}.
     *
     * @return A {@code SentEmbedAuthor}.
     */
    default Optional<SentEmbedAuthor> asSentEmbedAuthor() {
        return as(SentEmbedAuthor.class);
    }

}
