package org.javacord.api.entity.message.embed.internal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;

/**
 * This class is internally used by the {@link EmbedBuilder} to created embeds.
 * You usually don't want to interact with this object.
 */
public interface EmbedBuilderDelegate {

    /**
     * Sets the title of the embed.
     *
     * @param title The title to set.
     */
    void setTitle(String title);

    /**
     * Sets the description of the embed.
     *
     * @param description The description to set.
     */
    void setDescription(String description);

    /**
     * Sets the URL of the embed.
     *
     * @param url The URL as a String to set.
     */
    void setUrl(String url);

    /**
     * Sets the timestamp of the embed.
     *
     * @param timestamp The instant to set as the timestamp.
     */
    void setTimestamp(Instant timestamp);

    /**
     * Sets the color of the embed.
     *
     * @param color The color to set.
     */
    void setColor(Color color);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The URL as a String of the author.
     * @param iconUrl The URL to the Icon for the author as a String.
     */
    void setAuthor(String name, String url, String iconUrl);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The URL as a String of the author.
     * @param icon The Icon of the author as an object. Must be one of {@link Icon},
     * {@link File}, {@link InputStream}, {@link BufferedImage} or {@link byte[]}.
     * @param fileType The image type of the file object.
     */
    void setAuthor(String name, String url, Object icon, String fileType);

    /**
     * Sets the author of the embed.
     *
     * @param author An instance of {@code BaseEmbedAuthor} to be set to the embed.
     * @param <T> Generic type for the author to match a BaseEmbedAuthor and a
     * BaseEmbedMember that can be converted to an EmbedDraftAuthor.
     */
    <T extends BaseEmbedAuthor & BaseEmbedMember<?, ? extends EmbedDraftAuthor, ?>> void setAuthor(T author);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param url The URL to the thumbnail image as a String.
     */
    void setThumbnail(String url);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param image The image of the thumbnail as an object. Must be one of {@link Icon},
     * {@link File}, {@link InputStream}, {@link BufferedImage} or {@link byte[]}.
     * @param fileType The image type of the file object.
     */
    void setThumbnail(Object image, String fileType);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail An instance of {@code BaseEmbedThumbnail} to be set to the embed.
     * @param <T> Generic type for the thumbnail to match a BaseEmbedThumbnail and a
     * BaseEmbedMember that can be converted to an EmbedDraftThumbnail.
     */
    <T extends BaseEmbedThumbnail & BaseEmbedMember<?, ? extends EmbedDraftThumbnail, ?>> void setThumbnail(T thumbnail);

    /**
     * Sets the image of the embed.
     *
     * @param url The URL to the image as a String.
     */
    void setImage(String url);

    /**
     * Sets the image of the embed.
     *
     * @param image The image as an object. Must be one of {@link Icon},
     * {@link File}, {@link InputStream}, {@link BufferedImage} or {@link byte[]}.
     * @param fileType The image type of the file object.
     */
    void setImage(Object image, String fileType);

    /**
     * Sets the image of the embed.
     *
     * @param image An instance of {@code BaseEmbedImage} to be set to the embed.
     * @param <T> Generic type for the image to match a BaseEmbedImage and a
     * BaseEmbedMember that can be converted to an EmbedDraftImage.
     */
    <T extends BaseEmbedImage & BaseEmbedMember<?, ? extends EmbedDraftImage, ?>> void setImage(T image);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The URL to the footer icon as a String.
     */
    void setFooter(String text, String iconUrl);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The image as an object. Must be one of {@link Icon},
     * {@link File}, {@link InputStream}, {@link BufferedImage} or {@link byte[]}.
     * @param fileType The image type of the file object.
     */
    void setFooter(String text, Object icon, String fileType);

    /**
     * Sets the image of the embed.
     *
     * @param footer An instance of {@code BaseEmbedFooter} to be set to the embed.
     * @param <T> Generic type for the image to match a BaseEmbedFooter and a
     * BaseEmbedMember that can be converted to an EmbedDraftFooter.
     */
    <T extends BaseEmbedFooter & BaseEmbedMember<?, ? extends EmbedDraftFooter, ?>> void setFooter(T footer);

    /**
     * Adds a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline.
     */
    void addField(String name, String value, boolean inline);

    /**
     * Adds a field to the embed.
     *
     * @param field The field to add to the embed.
     */
    void addField(BaseEmbedField field);

    /**
     * Builds an EmbedDraft from this builder delegate.
     *
     * @return The newly created EmbedDraft object.
     */
    EmbedDraft build();
}
