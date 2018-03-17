package org.javacord.api.entity.message.embed;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * This interface represents an embed.
 */
public interface Embed {

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
    Optional<EmbedFooter> getFooter();

    /**
     * Gets the image of the embed.
     *
     * @return The image of the embed.
     */
    Optional<EmbedImage> getImage();

    /**
     * Gets the thumbnail of the embed.
     *
     * @return The thumbnail of the embed.
     */
    Optional<EmbedThumbnail> getThumbnail();

    /**
     * Gets the video of the embed.
     *
     * @return The video of the embed.
     */
    Optional<EmbedVideo> getVideo();

    /**
     * Gets the provider of the embed.
     *
     * @return The provider of the embed.
     */
    Optional<EmbedProvider> getProvider();

    /**
     * Gets the author of the embed.
     *
     * @return The author of the embed.
     */
    Optional<EmbedAuthor> getAuthor();

    /**
     * Gets the fields of the embed.
     *
     * @return The fields of the embed.
     */
    List<EmbedField> getFields();

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
        getUrl().ifPresent(url -> builder.setUrl(url.toString()));
        getTimestamp().ifPresent(builder::setTimestamp);
        getColor().ifPresent(builder::setColor);
        getFooter().ifPresent(footer -> builder.setFooter(
                footer.getText().orElse(null), footer.getIconUrl().map(URL::toString).orElse(null)));
        getImage().ifPresent(image -> builder.setImage(image.getUrl().toString()));
        getThumbnail().ifPresent(thumbnail -> builder.setThumbnail(thumbnail.getUrl().toString()));
        getAuthor().ifPresent(author -> builder.setAuthor(
                author.getName(),
                author.getUrl().map(URL::toString).orElse(null),
                author.getIconUrl().map(URL::toString).orElse(null)));
        getFields().forEach(field -> builder.addField(field.getName(), field.getValue(), field.isInline()));
        return builder;
    }

}