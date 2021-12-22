package org.javacord.api.entity.message;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.MessageBuilderBaseDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

abstract class MessageBuilderBase<T> {
    private final Class<T> myClass;

    protected MessageBuilderBase(Class<T> myClass) {
        this.myClass = myClass;
    }

    /**
     * The message delegate used by this instance.
     */
    protected final MessageBuilderBaseDelegate delegate = DelegateFactory.createMessageBuilderBaseDelegate();

    /**
     * Add multiple high level components to the message.
     *
     * @param components The high level components.
     * @return The current instance in order to chain call methods.
     */
    public T addComponents(HighLevelComponent... components) {
        delegate.addComponents(components);
        return myClass.cast(this);
    }

    /**
     * Add multiple low level components, wrapped in an ActionRow, to the message.
     *
     * @param components The low level components.
     * @return The current instance in order to chain call methods.
     */
    public T addActionRow(LowLevelComponent... components) {
        delegate.addActionRow(components);
        return myClass.cast(this);
    }

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code     The code.
     * @return The current instance in order to chain call methods.
     */
    public T appendCode(String language, String code) {
        delegate.appendCode(language, code);
        return myClass.cast(this);
    }

    /**
     * Appends a timestamp to the message with the default timestamp style {@link TimestampStyle#SHORT_DATE_TIME}.
     *
     * @param epochSeconds The epoch time in seconds.
     * @return The current instance in order to chain call methods.
     */
    public T appendTimestamp(final long epochSeconds) {
        appendTimestamp(epochSeconds, TimestampStyle.SHORT_DATE_TIME);
        return myClass.cast(this);
    }

    /**
     * Appends a timestamp to the message with the default timestamp style {@link TimestampStyle#SHORT_DATE_TIME}.
     *
     * @param instant The instant for the displaying timestamp.
     * @return The current instance in order to chain call methods.
     */
    public T appendTimestamp(final Instant instant) {
        appendTimestamp(instant.getEpochSecond(), TimestampStyle.SHORT_DATE_TIME);
        return myClass.cast(this);
    }

    /**
     * Appends a timestamp to the message.
     *
     * @param epochSeconds The epoch time in seconds.
     * @param timestampStyle The displayed timestamp style.
     *
     * @return The current instance in order to chain call methods.
     */
    public T appendTimestamp(final long epochSeconds, final TimestampStyle timestampStyle) {
        delegate.append(timestampStyle.getTimestampTag(epochSeconds));
        return myClass.cast(this);
    }

    /**
     * Appends a timestamp to the message.
     *
     * @param instant The instant for the displaying timestamp.
     * @param timestampStyle The displayed timestamp style.
     *
     * @return The current instance in order to chain call methods.
     */
    public T appendTimestamp(final Instant instant, final TimestampStyle timestampStyle) {
        appendTimestamp(instant.getEpochSecond(), timestampStyle);
        return myClass.cast(this);
    }

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    public T append(String message, MessageDecoration... decorations) {
        delegate.append(message, decorations);
        return myClass.cast(this);
    }

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    public T append(Mentionable entity) {
        delegate.append(entity);
        return myClass.cast(this);
    }

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    public T append(Object object) {
        delegate.append(object);
        return myClass.cast(this);
    }

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public T appendNewLine() {
        delegate.appendNewLine();
        return myClass.cast(this);
    }

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     * @return The current instance in order to chain call methods.
     */
    public T setContent(String content) {
        delegate.setContent(content);
        return myClass.cast(this);
    }

    /**
     * Removes the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @return The current instance in order to chain call methods.
     */
    public T removeContent() {
        delegate.setContent(null);
        return myClass.cast(this);
    }


    /**
     * Sets the embed of the message (overrides all existing embeds).
     *
     * @param embed The embed to set.
     * @return The current instance in order to chain call methods.
     */
    public T setEmbed(EmbedBuilder embed) {
        delegate.removeAllEmbeds();
        delegate.addEmbed(embed);
        return myClass.cast(this);
    }

    /**
     * Sets multiple embeds of the message (overrides all existing embeds).
     *
     * @param embeds The embed to set.
     * @return The current instance in order to chain call methods.
     */
    public T setEmbeds(EmbedBuilder... embeds) {
        delegate.removeAllEmbeds();
        delegate.addEmbeds(Arrays.asList(embeds));
        return myClass.cast(this);
    }

    /**
     * Sets multiple embeds of the message (overrides all existing embeds).
     *
     * @param embeds The embed to set.
     * @return The current instance in order to chain call methods.
     */
    public T setEmbeds(List<EmbedBuilder> embeds) {
        delegate.removeAllEmbeds();
        delegate.addEmbeds(embeds);
        return myClass.cast(this);
    }

    /**
     * Adds an embed to the message.
     *
     * @param embed The embed to add.
     * @return The current instance in order to chain call methods.
     */
    public T addEmbed(EmbedBuilder embed) {
        delegate.addEmbed(embed);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(BufferedImage, String)} instead.
     * @see #addAttachment(BufferedImage, String)
     */
    @Deprecated
    public T addFile(BufferedImage image, String fileName) {
        delegate.addFile(image, fileName);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(File)} instead.
     * @see #addAttachment(File)
     */
    @Deprecated
    public T addFile(File file) {
        delegate.addFile(file);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(Icon)} instead.
     * @see #addAttachment(Icon)
     */
    @Deprecated
    public T addFile(Icon icon) {
        delegate.addFile(icon);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(URL)} instead.
     * @see #addAttachment(URL)
     */
    @Deprecated
    public T addFile(URL url) {
        delegate.addFile(url);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(byte[], String)} instead.
     * @see #addAttachment(byte[], String)
     */
    @Deprecated
    public T addFile(byte[] bytes, String fileName) {
        delegate.addFile(bytes, fileName);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(InputStream, String)} instead.
     * @see #addAttachment(InputStream, String)
     */
    @Deprecated
    public T addFile(InputStream stream, String fileName) {
        delegate.addFile(stream, fileName);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(BufferedImage, String)} instead.
     * @see #addAttachmentAsSpoiler(BufferedImage, String)
     */
    @Deprecated
    public T addFileAsSpoiler(BufferedImage image, String fileName) {
        delegate.addFile(image, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(File)} instead.
     * @see #addAttachmentAsSpoiler(File)
     */
    @Deprecated
    public T addFileAsSpoiler(File file) {
        delegate.addFileAsSpoiler(file);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(Icon)} instead.
     * @see #addAttachmentAsSpoiler(Icon)
     */
    @Deprecated
    public T addFileAsSpoiler(Icon icon) {
        delegate.addFileAsSpoiler(icon);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(URL)} instead.
     * @see #addAttachment(URL)
     */
    @Deprecated
    public T addFileAsSpoiler(URL url) {
        delegate.addFileAsSpoiler(url);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(byte[], String)} instead.
     * @see #addAttachmentAsSpoiler(byte[], String)
     */
    @Deprecated
    public T addFileAsSpoiler(byte[] bytes, String fileName) {
        delegate.addFile(bytes, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @deprecated Use {@link #addAttachment(InputStream, String)} instead.
     * @see #addAttachment(InputStream, String)
     */
    @Deprecated
    public T addFileAsSpoiler(InputStream stream, String fileName) {
        delegate.addFile(stream, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(BufferedImage image, String fileName) {
        delegate.addAttachment(image, fileName);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(File file) {
        delegate.addAttachment(file);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(Icon icon) {
        delegate.addAttachment(icon);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(URL url) {
        delegate.addAttachment(url);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, fileName);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(InputStream stream, String fileName) {
        delegate.addAttachment(stream, fileName);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        delegate.addAttachment(image, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(File file) {
        delegate.addAttachmentAsSpoiler(file);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(Icon icon) {
        delegate.addAttachmentAsSpoiler(icon);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(URL url) {
        delegate.addAttachmentAsSpoiler(url);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(InputStream stream, String fileName) {
        delegate.addAttachment(stream, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    /**
     * Controls who will be mentioned if mentions exist in the message.
     *
     * @param allowedMentions The mention object.
     * @return The current instance in order to chain call methods.
     */
    public T setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return myClass.cast(this);
    }

    /**
     * Remove all high-level components from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public T removeAllComponents() {
        delegate.removeAllComponents();
        return myClass.cast(this);
    }

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    public T addEmbeds(EmbedBuilder... embeds) {
        delegate.addEmbeds(Arrays.asList(embeds));
        return myClass.cast(this);
    }

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    public T addEmbeds(List<EmbedBuilder> embeds) {
        delegate.addEmbeds(embeds);
        return myClass.cast(this);
    }

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     * @return The current instance in order to chain call methods.
     */
    public T removeEmbed(EmbedBuilder embed) {
        delegate.removeEmbed(embed);
        return myClass.cast(this);
    }

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     * @return The current instance in order to chain call methods.
     */
    public T removeEmbeds(EmbedBuilder... embeds) {
        delegate.removeEmbeds(embeds);
        return myClass.cast(this);
    }

    /**
     * Removes all embeds from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public T removeAllEmbeds() {
        delegate.removeAllEmbeds();
        return myClass.cast(this);
    }

    /**
     * Sets the nonce of the message.
     *
     * @param nonce The nonce to set.
     * @return The current instance in order to chain call methods.
     */
    public T setNonce(String nonce) {
        delegate.setNonce(nonce);
        return myClass.cast(this);
    }

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    public StringBuilder getStringBuilder() {
        return delegate.getStringBuilder();
    }
}
