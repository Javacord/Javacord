package org.javacord.entity.message.embed;

import org.javacord.entity.Icon;
import org.javacord.entity.message.MessageAuthor;
import org.javacord.entity.user.User;
import org.javacord.entity.Icon;
import org.javacord.entity.message.MessageAuthor;
import org.javacord.entity.user.User;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;

/**
 * This class is internally used by the {@link EmbedBuilder} to created embeds.
 * You usually don't want to interact with this object.
 */
public interface EmbedFactory {

    /**
     * Gets a new instance of an embed factory.
     * This method is the same as calling the constructor of the implementation.
     *
     * @return A new instance of an embed factory.
     */
    EmbedFactory getNewInstance();

    /**
     * Sets the title of the embed.
     *
     * @param title The title of the embed.
     */
    void setTitle(String title);

    /**
     * Sets the description of the embed.
     *
     * @param description The description of the embed.
     */
    void setDescription(String description);

    /**
     * Sets the url of the embed.
     *
     * @param url The url of the embed.
     */
    void setUrl(String url);

    /**
     * Sets the current time as timestamp of the embed.
     */
    void setTimestamp();

    /**
     * Sets the timestamp of the embed.
     *
     * @param timestamp The timestamp to set.
     */
    void setTimestamp(Instant timestamp);

    /**
     * Sets the color of the embed.
     *
     * @param color The color of the embed.
     */
    void setColor(Color color);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     */
    void setFooter(String text);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The url of the footer's icon.
     */
    void setFooter(String text, String iconUrl);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     */
    void setFooter(String text, Icon icon);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     */
    void setFooter(String text, File icon);

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     */
    void setFooter(String text, InputStream icon);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setFooter(String text, InputStream icon, String fileType);

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     */
    void setFooter(String text, byte[] icon);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setFooter(String text, byte[] icon, String fileType);

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     */
    void setFooter(String text, BufferedImage icon);

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setFooter(String text, BufferedImage icon, String fileType);

    /**
     * Sets the image of the embed.
     *
     * @param url The url of the image.
     */
    void setImage(String url);

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     */
    void setImage(Icon image);

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     */
    void setImage(File image);

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     */
    void setImage(InputStream image);

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setImage(InputStream image, String fileType);

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     */
    void setImage(byte[] image);

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setImage(byte[] image, String fileType);

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     */
    void setImage(BufferedImage image);

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setImage(BufferedImage image, String fileType);

    /**
     * Sets the author of the embed.
     *
     * @param author The message author which should be used as author.
     */
    void setAuthor(MessageAuthor author);

    /**
     * Sets the author of the embed.
     *
     * @param author The user which should be used as author.
     */
    void setAuthor(User author);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     */
    void setAuthor(String name);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param iconUrl The url of the author's icon.
     */
    void setAuthor(String name, String url, String iconUrl);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     */
    void setAuthor(String name, String url, Icon icon);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     */
    void setAuthor(String name, String url, File icon);

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     */
    void setAuthor(String name, String url, InputStream icon);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setAuthor(String name, String url, InputStream icon, String fileType);

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     */
    void setAuthor(String name, String url, byte[] icon);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setAuthor(String name, String url, byte[] icon, String fileType);

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     */
    void setAuthor(String name, String url, BufferedImage icon);

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setAuthor(String name, String url, BufferedImage icon, String fileType);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param url The url of the thumbnail.
     */
    void setThumbnail(String url);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     */
    void setThumbnail(Icon thumbnail);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     */
    void setThumbnail(File thumbnail);

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     */
    void setThumbnail(InputStream thumbnail);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setThumbnail(InputStream thumbnail, String fileType);

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     */
    void setThumbnail(byte[] thumbnail);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setThumbnail(byte[] thumbnail, String fileType);

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     */
    void setThumbnail(BufferedImage thumbnail);

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     */
    void setThumbnail(BufferedImage thumbnail, String fileType);

    /**
     * Adds a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline or not.
     */
    void addField(String name, String value, boolean inline);

    /**
     * Checks if this embed requires any attachments.
     *
     * @return Whether the embed requires attachments or not.
     */
    boolean requiresAttachments();

    /**
     * Returns this embed as {@link Embed}.
     *
     * @return this embed as {@code Embed}.
     */
    Embed asEmbed();
}