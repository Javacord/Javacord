package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.parts.EmbedField;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedAuthor;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedFooter;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedImage;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedProvider;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedThumbnail;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedVideo;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a sent embed.
 *
 * @see Message#getEmbeds()
 */
public interface SentEmbed extends EmbedBase {

    /**
     * Gets the message that this embed is in.
     *
     * @return The message.
     */
    Optional<Message> getMessage();

    /**
     * Gets the title of the embed.
     *
     * @return The title of the embed.
     */
    Optional<String> getTitle();

    /**
     * Gets the type of the embed.
     * (always "rich" for webhook embeds)
     *
     * @return The type of the embed.
     */
    String getType();

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
    Optional<SentEmbedFooter> getFooter();

    /**
     * Gets the image of the embed.
     *
     * @return The image of the embed.
     */
    Optional<SentEmbedImage> getImage();

    /**
     * Gets the video of the embed.
     *
     * @return The image of the embed.
     */
    Optional<SentEmbedVideo> getVideo();

    /**
     * Gets the thumbnail of the embed.
     *
     * @return The thumbnail of the embed.
     */
    Optional<SentEmbedThumbnail> getThumbnail();

    /**
     * Gets the provider of the embed.
     *
     * @return The provider of the embed.
     */
    Optional<SentEmbedProvider> getProvider();

    /**
     * Gets the author of the embed.
     *
     * @return The author of the embed.
     */
    Optional<SentEmbedAuthor> getAuthor();

    /**
     * Gets the fields of the embed.
     *
     * @return The fields of the embed.
     */
    List<EmbedField> getFields();
}
