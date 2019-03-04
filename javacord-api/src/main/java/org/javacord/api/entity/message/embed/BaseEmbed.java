package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.util.Specializable;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Basic representation of an embed.
 */
public interface BaseEmbed extends Specializable<BaseEmbed> {

    /**
     * Gets the title of the embed.
     *
     * @return The title of the embed.
     */
    Optional<String> getTitle();

    /**
     * Gets the description of the embed.
     *
     * @return The description of the embed.
     */
    Optional<String> getDescription();

    /**
     * Gets the url of the embed.
     *
     * @return The url of the embed.
     */
    Optional<String> getUrl();

    /**
     * Gets the timestamp of the embed.
     *
     * @return The timestamp of the embed.
     */
    Optional<Instant> getTimestamp();

    /**
     * Gets the color of the embed.
     *
     * @return The color of the embed.
     */
    Optional<Color> getColor();

    /**
     * Gets the footer of the embed.
     *
     * @return The footer of the embed.
     */
    Optional<? extends BaseEmbedFooter> getFooter();

    /**
     * Gets the image of the embed.
     *
     * @return The image of the embed.
     */
    Optional<? extends BaseEmbedImage> getImage();

    /**
     * Gets the thumbnail of the embed.
     *
     * @return The thumbnail of the embed.
     */
    Optional<? extends BaseEmbedThumbnail> getThumbnail();

    /**
     * Gets the author of the embed.
     *
     * @return The author of the embed.
     */
    Optional<? extends BaseEmbedAuthor> getAuthor();

    /**
     * Gets the fields of the embed.
     *
     * @return The fields of the embed.
     */
    List<? extends BaseEmbedField> getFields();

    /**
     * Creates a builder, based on the embed.
     * You can use this method, if you want to resend an embed, you received as a message.
     *
     * @return A builder with the values of this embed.
     */
    EmbedBuilder toBuilder();

    /**
     * Creates a new EmbedDraft object from this embed.
     *
     * @return A new EmbedDraft created from this embed.
     */
    default EmbedDraft toEmbedDraft() {
        return toBuilder().build();
    }

    /**
     * Tries to cast this embed to a SentEmbed.
     *
     * @return An optional SentEmbed.
     */
    default Optional<SentEmbed> asSentEmbed() {
        return as(SentEmbed.class);
    }

}
