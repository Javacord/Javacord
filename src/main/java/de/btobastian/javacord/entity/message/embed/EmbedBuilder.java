package de.btobastian.javacord.entity.message.embed;

import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.message.MessageAuthor;
import de.btobastian.javacord.entity.user.User;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * This class is used to create embeds.
 */
public class EmbedBuilder {

    /**
     * The base factory. It's only used to create new factories.
     */
    private static final EmbedFactory baseFactory;

    // Load it static, because it has a better performance to load it only once
    static {
        ServiceLoader<EmbedFactory> factoryServiceLoader = ServiceLoader.load(EmbedFactory.class);
        Iterator<EmbedFactory> factoryIterator = factoryServiceLoader.iterator();
        if (factoryIterator.hasNext()) {
            baseFactory = factoryIterator.next();
            if (factoryIterator.hasNext()) {
                throw new IllegalStateException("Found more than one EmbedFactory implementation!");
            }
        } else {
            throw new IllegalStateException("No EmbedFactory implementation was found!");
        }
    }

    /**
     * The embed factory used by this instance.
     */
    private final EmbedFactory factory;

    /**
     * Creates a new embed builder.
     */
    public EmbedBuilder() {
        factory = baseFactory.getNewInstance();
    }

    /**
     * Gets the factory used by this embed builder internally.
     *
     * @return The factory used by this embed builder internally.
     */
    public EmbedFactory getFactory() {
        return factory;
    }

    /**
     * Sets the title of the embed.
     *
     * @param title The title of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setTitle(String title) {
        factory.setTitle(title);
        return this;
    }

    /**
     * Sets the description of the embed.
     *
     * @param description The description of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setDescription(String description) {
        factory.setDescription(description);
        return this;
    }

    /**
     * Sets the url of the embed.
     *
     * @param url The url of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setUrl(String url) {
        factory.setUrl(url);
        return this;
    }

    /**
     * Sets the current time as timestamp of the embed.
     *
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setTimestamp() {
        factory.setTimestamp();
        return this;
    }

    /**
     * Sets the timestamp of the embed.
     *
     * @param timestamp The timestamp to set.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setTimestamp(Instant timestamp) {
        factory.setTimestamp(timestamp);
        return this;
    }

    /**
     * Sets the color of the embed.
     *
     * @param color The color of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setColor(Color color) {
        factory.setColor(color);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text) {
        factory.setFooter(text);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The url of the footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, String iconUrl) {
        factory.setFooter(text, iconUrl);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, Icon icon) {
        factory.setFooter(text, icon);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, File icon) {
        factory.setFooter(text, icon);
        return this;
    }

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, InputStream icon) {
        factory.setFooter(text, icon);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, InputStream icon, String fileType) {
        factory.setFooter(text, icon, fileType);
        return this;
    }

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, byte[] icon) {
        factory.setFooter(text, icon);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, byte[] icon, String fileType) {
        factory.setFooter(text, icon, fileType);
        return this;
    }

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, BufferedImage icon) {
        factory.setFooter(text, icon);
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, BufferedImage icon, String fileType) {
        factory.setFooter(text, icon, fileType);
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param url The url of the image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(String url) {
        factory.setImage(url);
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(Icon image) {
        factory.setImage(image);
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(File image) {
        factory.setImage(image);
        return this;
    }

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(InputStream image) {
        factory.setImage(image);
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(InputStream image, String fileType) {
        factory.setImage(image, fileType);
        return this;
    }

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(byte[] image) {
        factory.setImage(image);
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(byte[] image, String fileType) {
        factory.setImage(image, fileType);
        return this;
    }

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(BufferedImage image) {
        factory.setImage(image);
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(BufferedImage image, String fileType) {
        factory.setImage(image, fileType);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param author The message author which should be used as author.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(MessageAuthor author) {
        factory.setAuthor(author);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param author The user which should be used as author.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(User author) {
        factory.setAuthor(author);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name) {
        factory.setAuthor(name);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param iconUrl The url of the author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, String iconUrl) {
        factory.setAuthor(name, url, iconUrl);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, Icon icon) {
        factory.setAuthor(name, url, icon);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, File icon) {
        factory.setAuthor(name, url, icon);
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, InputStream icon) {
        factory.setAuthor(name, url, icon);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, InputStream icon, String fileType) {
        factory.setAuthor(name, url, icon, fileType);
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, byte[] icon) {
        factory.setAuthor(name, url, icon);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, byte[] icon, String fileType) {
        factory.setAuthor(name, url, icon, fileType);
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, BufferedImage icon) {
        factory.setAuthor(name, url, icon);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, BufferedImage icon, String fileType) {
        factory.setAuthor(name, url, icon, fileType);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param url The url of the thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(String url) {
        factory.setThumbnail(url);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(Icon thumbnail) {
        factory.setThumbnail(thumbnail);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(File thumbnail) {
        factory.setThumbnail(thumbnail);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(InputStream thumbnail) {
        factory.setThumbnail(thumbnail);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(InputStream thumbnail, String fileType) {
        factory.setThumbnail(thumbnail, fileType);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(byte[] thumbnail) {
        factory.setThumbnail(thumbnail);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(byte[] thumbnail, String fileType) {
        factory.setThumbnail(thumbnail, fileType);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(BufferedImage thumbnail) {
        factory.setThumbnail(thumbnail);
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(BufferedImage thumbnail, String fileType) {
        factory.setThumbnail(thumbnail, fileType);
        return this;
    }

    /**
     * Adds a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline or not.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder addField(String name, String value, boolean inline) {
        factory.addField(name, value, inline);
        return this;
    }

    /**
     * Checks if this embed requires any attachments.
     *
     * @return Whether the embed requires attachments or not.
     */
    public boolean requiresAttachments() {
        return factory.requiresAttachments();
    }
}