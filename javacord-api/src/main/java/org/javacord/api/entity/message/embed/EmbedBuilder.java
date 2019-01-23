package org.javacord.api.entity.message.embed;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.internal.EmbedBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

/**
 * This class is used to create embeds.
 */
public class EmbedBuilder {

    /**
     * The embed delegate used by this instance.
     */
    protected final EmbedBuilderDelegate delegate;

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

    public EmbedBuilder setTitle(String title) {
        delegate.setTitle(title);
        return this;
    }

    public EmbedBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    public EmbedBuilder setUrl(URL url) {
        delegate.setUrl(url);
        return this;
    }

    public EmbedBuilder setTimestampToNow() {
        return setTimestamp(Instant.now());
    }

    public EmbedBuilder setTimestamp(Instant instant) {
        delegate.setTimestamp(instant);
        return this;
    }

    public EmbedBuilder setColor(Color color) {
        delegate.setColor(color);
        return this;
    }

    public EmbedBuilder setAuthor(String name) {
        return setAuthor(name, null, (URL) null);
    }

    public EmbedBuilder setAuthor(String name, URL url) {
        return setAuthor(name, url, (URL) null);
    }

    public EmbedBuilder setAuthor(String name, URL url, URL iconUrl) {
        delegate.setAuthor(name, url, iconUrl);
        return this;
    }

    public EmbedBuilder setAuthor(String name, URL url, Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setAuthor(name, url, icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setAuthor(String name, URL url, File icon) {
        String fileName = icon.getName();
        delegate.setAuthor(name, url, icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setAuthor(String name, URL url, InputStream icon) {
        return setAuthor(name, url, icon, "png");
    }

    public EmbedBuilder setAuthor(String name, URL url, InputStream icon, String fileType) {
        delegate.setAuthor(name, url, icon, fileType);
        return this;
    }

    public EmbedBuilder setAuthor(String name, URL url, byte[] icon) {
        return setAuthor(name, url, icon, "png");
    }

    public EmbedBuilder setAuthor(String name, URL url, byte[] icon, String fileType) {
        delegate.setAuthor(name, url, icon, fileType);
        return this;
    }

    public EmbedBuilder setAuthor(String name, URL url, BufferedImage icon) {
        return setAuthor(name, url, icon, "png");
    }

    public EmbedBuilder setAuthor(String name, URL url, BufferedImage icon, String fileType) {
        delegate.setAuthor(name, url, icon, fileType);
        return this;
    }

    public EmbedBuilder setAuthor(BaseEmbedAuthor author) {
        delegate.setAuthor(author);
        return this;
    }

    public EmbedBuilder setThumbnail(URL url) {
        delegate.setThumbnail(url);
        return this;
    }

    public EmbedBuilder setThumbnail(Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setThumbnail(icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setThumbnail(File icon) {
        String fileName = icon.getName();
        delegate.setThumbnail(icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setThumbnail(InputStream icon) {
        return setThumbnail(icon, "png");
    }

    public EmbedBuilder setThumbnail(InputStream icon, String fileType) {
        delegate.setThumbnail(icon, fileType);
        return this;
    }

    public EmbedBuilder setThumbnail(byte[] icon) {
        return setThumbnail(icon, "png");
    }

    public EmbedBuilder setThumbnail(byte[] icon, String fileType) {
        delegate.setThumbnail(icon, fileType);
        return this;
    }

    public EmbedBuilder setThumbnail(BufferedImage icon) {
        return setThumbnail(icon, "png");
    }

    public EmbedBuilder setThumbnail(BufferedImage icon, String fileType) {
        delegate.setThumbnail(icon, fileType);
        return this;
    }

    public EmbedBuilder setThumbnail(BaseEmbedThumbnail thumbnail) {
        delegate.setThumbnail(thumbnail);
        return this;
    }

    public EmbedBuilder setImage(URL url) {
        delegate.setImage(url);
        return this;
    }

    public EmbedBuilder setImage(Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setImage(icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setImage(File icon) {
        String fileName = icon.getName();
        delegate.setImage(icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setImage(InputStream icon) {
        return setImage(icon, "png");
    }

    public EmbedBuilder setImage(InputStream icon, String fileType) {
        delegate.setImage(icon, fileType);
        return this;
    }

    public EmbedBuilder setImage(byte[] icon) {
        return setImage(icon, "png");
    }

    public EmbedBuilder setImage(byte[] icon, String fileType) {
        delegate.setImage(icon, fileType);
        return this;
    }

    public EmbedBuilder setImage(BufferedImage icon) {
        return setImage(icon, "png");
    }

    public EmbedBuilder setImage(BufferedImage icon, String fileType) {
        delegate.setImage(icon, fileType);
        return this;
    }

    public EmbedBuilder setImage(BaseEmbedImage image) {
        delegate.setImage(image);
        return this;
    }

    public EmbedBuilder setFooter(String text) {
        return setFooter(text, (URL) null);
    }

    public EmbedBuilder setFooter(String text, URL iconUrl) {
        delegate.setFooter(text, iconUrl);
        return this;
    }

    public EmbedBuilder setFooter(String text, Icon icon) {
        String iconUrl = icon.getUrl().toExternalForm();
        delegate.setFooter(text, icon, iconUrl.substring(iconUrl.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setFooter(String text, File icon) {
        String fileName = icon.getName();
        delegate.setFooter(text, icon, fileName.substring(fileName.lastIndexOf('.') + 1));
        return this;
    }

    public EmbedBuilder setFooter(String text, InputStream icon) {
        return setFooter(text, icon, "png");
    }

    public EmbedBuilder setFooter(String text, InputStream icon, String fileType) {
        delegate.setFooter(text, icon, fileType);
        return this;
    }

    public EmbedBuilder setFooter(String text, byte[] icon) {
        return setFooter(text, icon, "png");
    }

    public EmbedBuilder setFooter(String text, byte[] icon, String fileType) {
        delegate.setFooter(text, icon, fileType);
        return this;
    }

    public EmbedBuilder setFooter(String text, BufferedImage icon) {
        return setFooter(text, icon, "png");
    }

    public EmbedBuilder setFooter(String text, BufferedImage icon, String fileType) {
        delegate.setFooter(text, icon, fileType);
        return this;
    }

    public EmbedBuilder setFooter(BaseEmbedFooter footer) {
        delegate.setFooter(footer);
        return this;
    }

    public EmbedBuilder addField(String name, String value) {
        return addField(name, value, false);
    }

    public EmbedBuilder addInlineField(String name, String value) {
        return addField(name, value, true);
    }

    public EmbedBuilder addField(String name, String value, boolean inline) {
        delegate.addField(name, value, inline);
        return this;
    }

    public EmbedBuilder addField(BaseEmbedField field) {
        delegate.addField(field);
        return this;
    }

    public EmbedDraft build() {
        return delegate.build();
    }
}
