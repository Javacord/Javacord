package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;
import org.javacord.api.util.Specializable;

import java.util.Optional;

/**
 * Basic representation of an embed's footer.
 */
public interface BaseEmbedFooter extends Specializable<BaseEmbedFooter> {

    /**
     * Gets the embed that this footer is part of.
     *
     * @return The parent embed.
     */
    BaseEmbed getEmbed();

    /**
     * Gets the footer text.
     *
     * @return The text of the footer.
     */
    String getText();

    /**
     * Gets the url of the footer icon.
     *
     * @return The url of the footer icon.
     */
    Optional<String> getIconUrl();

    /**
     * Gets this footer as a {@code EmbedDraftFooter}.
     * If this instance already is an {@code EmbedDraftFooter}, this method will return the cast instance.
     *
     * @return A {@code EmbedDraftFooter}.
     */
    EmbedDraftFooter toEmbedDraftFooter();

    /**
     * Gets this footer as a {@code SentEmbedFooter}.
     *
     * @return A {@code SentEmbedFooter}.
     */
    default Optional<SentEmbedFooter> asSentEmbedFooter() {
        return as(SentEmbedFooter.class);
    }

}
