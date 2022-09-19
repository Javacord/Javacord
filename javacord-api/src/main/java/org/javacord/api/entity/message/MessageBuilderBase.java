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
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(BufferedImage image, String fileName) {
        addAttachment(image, fileName, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, fileName, description);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(File file) {
        addAttachment(file, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(File file, String description) {
        delegate.addAttachment(file, description);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(Icon icon) {
        addAttachment(icon, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(Icon icon, String description) {
        delegate.addAttachment(icon, description);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(URL url) {
        addAttachment(url, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(URL url, String description) {
        delegate.addAttachment(url, description);
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
        addAttachment(bytes, fileName, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, fileName, description);
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
        addAttachment(stream, fileName, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachment(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, fileName, description);
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
        addAttachment(image, "SPOILER_" + fileName, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, "SPOILER_" + fileName, description);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(File file) {
        addAttachmentAsSpoiler(file, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(File file, String description) {
        delegate.addAttachmentAsSpoiler(file, description);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(Icon icon, String description) {
        delegate.addAttachmentAsSpoiler(icon, description);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(URL url, String description) {
        delegate.addAttachmentAsSpoiler(url, description);
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
        addAttachment(bytes, "SPOILER_" + fileName, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName, description);
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
        addAttachment(stream, "SPOILER_" + fileName, null);
        return myClass.cast(this);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public T addAttachmentAsSpoiler(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, "SPOILER_" + fileName, description);
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
