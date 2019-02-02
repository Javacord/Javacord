package org.javacord.api.entity.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.util.Specializable;

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
    Optional<URL> getUrl();

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
    default EmbedBuilder toBuilder() {
        EmbedBuilder builder = new EmbedBuilder();
        getTitle().ifPresent(builder::setTitle);
        getDescription().ifPresent(builder::setDescription);
        getUrl().ifPresent(builder::setUrl);
        getTimestamp().ifPresent(builder::setTimestamp);
        getColor().ifPresent(builder::setColor);
        getAuthor().ifPresent(builder::setAuthor);
        getThumbnail().ifPresent(builder::setThumbnail);
        getImage().ifPresent(builder::setImage);
        getFooter().ifPresent(builder::setFooter);
        getFields().forEach(builder::addField);
        return builder;
    }

    default EmbedDraft toEmbedDraft() {
        return as(EmbedDraft.class).orElse(this.toBuilder().build());
    }

    default Optional<SentEmbed> asSentEmbed() {
        return as(SentEmbed.class);
    }

}
