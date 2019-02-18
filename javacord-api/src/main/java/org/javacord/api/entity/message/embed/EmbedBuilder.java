package org.javacord.api.entity.message.embed;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.internal.EmbedBuilderDelegate;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.internal.DelegateFactory;

/**
 * This class is used to create embeds.
 */
public class EmbedBuilder {

    /**
     * The embed builder delegate used by this instance.
     */
    protected final EmbedBuilderDelegate delegate;

    /**
     * Constructor to create a new builder.
     */
    public EmbedBuilder() {
        delegate = DelegateFactory.createEmbedBuilderDelegate();
    }

    /**
     * Gets the delegate used by this embed builder internally.
     *
     * @return The delegate used by this embed builder internally.
     */
    public EmbedBuilderDelegate getDelegate() {
        return delegate;
    }

    /**
     * Sets the title for the embed.
     *
     * @param title The title to be set.
     * @return This instance of the builder.
     */
    public EmbedBuilder setTitle(String title) {
        delegate.setTitle(title);
        return this;
    }

    /**
     * Sets the description for the embed.
     *
     * @param description The description to be set.
     * @return This instance of the builder.
     */
    public EmbedBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the URL of the embed.
     *
     * @param url The URL to set.
     * @return This instance of the builder.
     */
    public EmbedBuilder setUrl(String url) {
        delegate.setUrl(url);
        return this;
    }

    /**
     * Sets the timestamp of the embed to "now".
     *
     * @return This instance of the builder.
     * @see Instant#now()
     */
    public EmbedBuilder setTimestampToNow() {
        return setTimestamp(Instant.now());
    }

    /**
     * Sets the timestamp of the embed.
     *
     * @param instant The instant to set the timestamp to.
     * @return This instance of the builder.
     */
    public EmbedBuilder setTimestamp(Instant instant) {
        delegate.setTimestamp(instant);
        return this;
    }

    /**
     * Sets the color of the embed.
     *
     * @param color The color to be set.
     * @return This instance of the builder.
     */
    public EmbedBuilder setColor(Color color) {
        delegate.setColor(color);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name) {
        return setAuthor(name, (String) null, (String) null);
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url) {
        return setAuthor(name, url, (String) null);
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param iconUrl The URL to the icon for the author.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, String iconUrl) {
        delegate.setAuthor(name, url, iconUrl);
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method fetches the image type of the Icon by the ending of the URL of the Icon.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setAuthor(name, url, icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method fetches the image type of the Icon by the ending of the URL of the Icon.
     *
     * @param name The name of the author.
     * @param icon The icon of the author.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setAuthor(name, null, icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method fetches the image type based on the file name ending.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as a File.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, File icon) {
        String fileName = icon.getName();
        delegate.setAuthor(name, url, icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method fetches the image type based on the file name ending.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as a File.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, File icon) {
        String fileName = icon.getName();
        delegate.setAuthor(name, null, icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as an InputStream. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, InputStream icon) {
        return setAuthor(name, url, icon, "png");
    }

    /**
     * Sets the author of the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as an InputStream. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, InputStream icon) {
        return setAuthor(name, null, icon, "png");
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as an InputStream.
     * @param fileType The image type of the InputStream.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, InputStream icon, String fileType) {
        setAuthor(name, null, icon, fileType);
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as an InputStream.
     * @param fileType The image type of the InputStream.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, InputStream icon, String fileType) {
        delegate.setAuthor(name, url, icon, fileType);
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as a byte array. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, byte[] icon) {
        return setAuthor(name, url, icon, "png");
    }

    /**
     * Sets the author of the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as a byte array. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, byte[] icon) {
        return setAuthor(name, null, icon, "png");
    }

    /**
     * Sets the author for the embed.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as a byte array.
     * @param fileType The image type of the byte array.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, byte[] icon, String fileType) {
        delegate.setAuthor(name, url, icon, fileType);
        return this;
    }

    /**
     * Sets the author for the embed.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as a byte array.
     * @param fileType The image type of the byte array.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, byte[] icon, String fileType) {
        setAuthor(name, null, icon, fileType);
        return this;
    }

    /**
     * Sets the author for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as a BufferedImage. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, BufferedImage icon) {
        return setAuthor(name, url, icon, "png");
    }

    /**
     * Sets the author for the embed.
     * This method assumes the file type is {@code PNG}.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as a BufferedImage. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, BufferedImage icon) {
        return setAuthor(name, null, icon, "png");
    }

    /**
     * Sets the author for the embed.
     *
     * @param name The name of the author.
     * @param icon The icon of the author as a BufferedImage.
     * @param fileType The image type of the BufferedImage.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, BufferedImage icon, String fileType) {
        setAuthor(name, null, icon, fileType);
        return this;
    }

    /**
     * Sets the author for the embed.
     *
     * @param name The name of the author.
     * @param url The URL of the author.
     * @param icon The icon of the author as a BufferedImage.
     * @param fileType The image type of the BufferedImage.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(String name, String url, BufferedImage icon, String fileType) {
        delegate.setAuthor(name, url, icon, fileType);
        return this;
    }

    /**
     * Sets the author for the embed from an User object.
     * The author name is fetched by {@link User#getName()}, and the icon is fetched using {@link User#getAvatar()}.
     *
     * @param user The User to be used as the author.
     * @return This instance of the builder.
     */
    public EmbedBuilder setAuthor(User user) {
        setAuthor(user.getName(), (String) null, user.getAvatar().getUrl().toExternalForm());
        return this;
    }

    /**
     * Sets the author for the embed from a previously constructed author.
     * The exact instance is set to this embed builder, and is being converted to an {@link EmbedDraftAuthor} using
     * {@link BaseEmbedMember#toDraftMember()} on embed draft construction.
     *
     * @param author The author for the embed.
     * @param <T> Generic type for the author to match a BaseEmbedAuthor and a
     * BaseEmbedMember that can be converted to an EmbedDraftAuthor.
     * @return This instance of the builder.
     */
    public <T extends BaseEmbedAuthor & BaseEmbedMember<?, EmbedDraftAuthor, ?>> EmbedBuilder setAuthor(T author) {
        delegate.setAuthor(author);
        return this;
    }

    /**
     * Sets the thumbnail for the embed.
     *
     * @param url The URL to the image.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(String url) {
        delegate.setThumbnail(url);
        return this;
    }

    /**
     * Sets the thumbnail for the embed.
     * This method fetches the file type based on the ending of the Icon URL.
     *
     * @param image The image to be used.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(Icon image) {
        String iconUrl = image.getUrl().toExternalForm();
        delegate.setThumbnail(image, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the thumbnail for the embed.
     * This method fetches the file type based on the ending of the file name.
     *
     * @param image The image as a File.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(File image) {
        String fileName = image.getName();
        delegate.setThumbnail(image, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the thumbnail for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param image The image as an InputStream. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(InputStream image) {
        return setThumbnail(image, "png");
    }

    /**
     * Sets the thumbnail for the embed.
     *
     * @param image The image as an InputStream.
     * @param fileType The image type of the InputStream.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(InputStream image, String fileType) {
        delegate.setThumbnail(image, fileType);
        return this;
    }

    /**
     * Sets the thumbnail for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param image The image as a byte array. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(byte[] image) {
        return setThumbnail(image, "png");
    }

    /**
     * Sets the thumbnail for the embed.
     *
     * @param image The image as a byte array.
     * @param fileType The image type of the byte array.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(byte[] image, String fileType) {
        delegate.setThumbnail(image, fileType);
        return this;
    }

    /**
     * Sets the thumbnail for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param image The image as a BufferedImage. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(BufferedImage image) {
        return setThumbnail(image, "png");
    }

    /**
     * Sets the thumbnail for the embed.
     *
     * @param image The image as a BufferedImage.
     * @param fileType The image type of the BufferedImage.
     * @return This instance of the builder.
     */
    public EmbedBuilder setThumbnail(BufferedImage image, String fileType) {
        delegate.setThumbnail(image, fileType);
        return this;
    }

    /**
     * Sets the thumbnail from a previously constructed Thumbnail.
     * The exact instance is set to this embed builder, and is being converted to an {@link EmbedDraftThumbnail} using
     * {@link BaseEmbedMember#toDraftMember()} on embed draft construction.
     *
     * @param thumbnail The thumbnail to set.
     * @param <T> Generic type for the thumbnail to match a BaseEmbedThumbnail and a
     * BaseEmbedMember that can be converted to an EmbedDraftThumbnail.
     * @return This instance of the builder.
     */
    public <T extends BaseEmbedThumbnail & BaseEmbedMember<?, EmbedDraftThumbnail, ?>> EmbedBuilder setThumbnail(
            T thumbnail
    ) {
        delegate.setThumbnail(thumbnail);
        return this;
    }

    /**
     * Sets the image for the embed.
     *
     * @param url The URL to the image to be used.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(String url) {
        delegate.setImage(url);
        return this;
    }

    /**
     * Sets the image for the embed.
     * This method fetches the URL for from the Icon object and then fetches the image type from the URL ending.
     *
     * @param image The image to be used.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(Icon image) {
        String iconUrl = image.getUrl().toExternalForm();
        delegate.setImage(image, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the image for the embed.
     * This method fetched the image type from the file name ending.
     *
     * @param image The image as a file.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(File image) {
        String fileName = image.getName();
        delegate.setImage(image, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the image for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param image The image as an InputStream. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(InputStream image) {
        return setImage(image, "png");
    }

    /**
     * Sets the image for the embed.
     *
     * @param image The image as an InputStream.
     * @param fileType The image type of the InputStream.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(InputStream image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    /**
     * Sets the image for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param image The image as a byte array. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(byte[] image) {
        return setImage(image, "png");
    }

    /**
     * Sets the image for the embed.
     *
     * @param image The image as a byte array.
     * @param fileType The image type of the byte array.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(byte[] image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    /**
     * Sets the image for the embed.
     * This method assumes the image type is {@code PNG}.
     *
     * @param image The image as a BufferedImage. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(BufferedImage image) {
        return setImage(image, "png");
    }

    /**
     * Sets the image for the embed.
     *
     * @param image The image to be used as a BufferedImage.
     * @param fileType The image type of the BufferedImage.
     * @return This instance of the builder.
     */
    public EmbedBuilder setImage(BufferedImage image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    /**
     * Sets the image for the embed.
     * The exact instance is set to this embed builder, and is being converted to an {@link EmbedDraftImage} using
     * {@link BaseEmbedMember#toDraftMember()} on embed draft construction.
     *
     * @param image The image to be used.
     * @param <T> Generic type for the image to match a BaseEmbedImage and a
     * BaseEmbedMember that can be converted to an EmbedDraftImage.
     * @return This instance of the builder.
     */
    public <T extends BaseEmbedImage & BaseEmbedMember<?, EmbedDraftImage, ?>> EmbedBuilder setImage(T image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the footer without an icon for the embed.
     *
     * @param text The text for the footer.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text) {
        return setFooter(text, (String) null);
    }

    /**
     * Sets the footer for the embed.
     *
     * @param text The text for the footer.
     * @param iconUrl The URL for the icon to be used.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, String iconUrl) {
        delegate.setFooter(text, iconUrl);
        return this;
    }

    /**
     * Sets the footer for the embed.
     * This method fetches the URL from the provided Icon and fetches the file type from the ending of the URL.
     *
     * @param text The text for the footer.
     * @param icon The icon to be used.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setFooter(text, icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the footer for the embed.
     * This method fetches the file type from the ending of the URL.
     *
     * @param text The text for the footer.
     * @param icon The icon as a File. The ending of the file has to fit the type of the image.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, File icon) {
        String fileName = icon.getName();
        delegate.setFooter(text, icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    /**
     * Sets the footer for the embed.
     * This method assumes the icon type is {@code PNG}.
     *
     * @param text The text for the footer.
     * @param icon The icon as an InputStream. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, InputStream icon) {
        return setFooter(text, icon, "png");
    }

    /**
     * Sets the footer for the embed.
     *
     * @param text The text for the footer.
     * @param icon The icon as an InputStream.
     * @param fileType The image type of the InputStream.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, InputStream icon, String fileType) {
        delegate.setFooter(text, icon, fileType);
        return this;
    }

    /**
     * Sets the footer for the embed.
     * This method assumes the icon type is {@code PNG}.
     *
     * @param text The text for the footer.
     * @param icon The icon as a byte array. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, byte[] icon) {
        return setFooter(text, icon, "png");
    }

    /**
     * Sets the footer for the embed.
     *
     * @param text The text for the footer.
     * @param icon The icon as a byte array.
     * @param fileType The image type of the byte array.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, byte[] icon, String fileType) {
        delegate.setFooter(text, icon, fileType);
        return this;
    }

    /**
     * Sets the footer for the embed.
     * This method assumes the icon type is {@code PNG}.
     *
     * @param text The text for the footer.
     * @param icon The icon as a BufferedImage. Must be of type {@code PNG} for this method to work.
     * @return This instance of the builder.
     */
    public EmbedBuilder setFooter(String text, BufferedImage icon) {
        return setFooter(text, icon, "png");
    }

    /**
     * Sets the footer for the embed.
     *
     * @param text The text for the footer.
     * @param icon The icon as a BufferedImage.
     * @param fileType The image type of the BufferedImage.
     * @return THis instance of the builder.
     */
    public EmbedBuilder setFooter(String text, BufferedImage icon, String fileType) {
        delegate.setFooter(text, icon, fileType);
        return this;
    }

    /**
     * Sets the embeds footer to a previously constructed footer.
     * The exact instance is set to this embed builder, and is being converted to an {@link EmbedDraftFooter} using
     * {@link BaseEmbedMember#toDraftMember()} on embed draft construction.
     *
     * @param footer The footer to set.
     * @param <T> Generic type for the footer to match a BaseEmbedFooter and a
     * BaseEmbedMember that can be converted to an EmbedDraftFooter.
     * @return This instance of the builder.
     */
    public <T extends BaseEmbedFooter & BaseEmbedMember<?, EmbedDraftFooter, ?>> EmbedBuilder setFooter(T footer) {
        delegate.setFooter(footer);
        return this;
    }

    /**
     * Adds a new non-inline field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @return This instance of the builder.
     */
    public EmbedBuilder addField(String name, String value) {
        return addField(name, value, false);
    }

    /**
     * Adds a new inline field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @return This instance of the builder.
     */
    public EmbedBuilder addInlineField(String name, String value) {
        return addField(name, value, true);
    }

    /**
     * Adds a new field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline.
     * @return This instance of the builder.
     */
    public EmbedBuilder addField(String name, String value, boolean inline) {
        delegate.addField(name, value, inline);
        return this;
    }

    /**
     * Adds a previously constructed embed field to the embed.
     * The exact instance is added to the embed builder.
     *
     * @param field The field to add.
     * @return This instance of the builder.
     */
    public EmbedBuilder addField(BaseEmbedField field) {
        delegate.addField(field);
        return this;
    }

    /**
     * Builds an EmbedDraft based on the builder.
     *
     * @return The constructed embed draft.
     */
    public EmbedDraft build() {
        return delegate.build();
    }
}
