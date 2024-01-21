package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.Icon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

@SuppressWarnings("unchecked")
public interface MessageBuilderAttachment<T extends MessageBuilderAttachment<T>> {

    /**
     * Gets the delegate of this message builder.
     *
     * @return The delegate of this message builder.
     */
    MessageBuilderBaseDelegate getDelegate();

    /**
     * Adds an attachment to the message.
     *
     * @param image    The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(BufferedImage image, String fileName) {
        addAttachment(image, fileName, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image       The image to add as an attachment.
     * @param fileName    The file name of the image.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(BufferedImage image, String fileName, String description) {
        getDelegate().addAttachment(image, fileName, description);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(File file) {
        addAttachment(file, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file        The file to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(File file, String description) {
        getDelegate().addAttachment(file, description, false);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(Icon icon) {
        addAttachment(icon, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon        The icon to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(Icon icon, String description) {
        getDelegate().addAttachment(icon, description, false);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(URL url) {
        addAttachment(url, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url         The url of the attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(URL url, String description) {
        getDelegate().addAttachment(url, description, false);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes    The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(byte[] bytes, String fileName) {
        addAttachment(bytes, fileName, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes       The bytes of the file.
     * @param fileName    The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(byte[] bytes, String fileName, String description) {
        getDelegate().addAttachment(bytes, fileName, description);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream   The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(InputStream stream, String fileName) {
        addAttachment(stream, fileName, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream      The stream of the file.
     * @param fileName    The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachment(InputStream stream, String fileName, String description) {
        getDelegate().addAttachment(stream, fileName, description);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image    The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        addAttachment(image, "SPOILER_" + fileName, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image       The image to add as an attachment.
     * @param fileName    The file name of the image.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(BufferedImage image, String fileName, String description) {
        getDelegate().addAttachment(image, "SPOILER_" + fileName, description);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(File file) {
        addAttachmentAsSpoiler(file, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file        The file to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(File file, String description) {
        getDelegate().addAttachment(file, description, true);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon        The icon to add as an attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(Icon icon, String description) {
        getDelegate().addAttachment(icon, description, true);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url, null);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url         The url of the attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(URL url, String description) {
        getDelegate().addAttachment(url, description, true);
        return (T) this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes    The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        return addAttachment(bytes, "SPOILER_" + fileName, null);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes       The bytes of the file.
     * @param fileName    The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(byte[] bytes, String fileName, String description) {
        return addAttachment(bytes, "SPOILER_" + fileName, description);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream   The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(InputStream stream, String fileName) {
        return addAttachment(stream, "SPOILER_" + fileName, null);
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream      The stream of the file.
     * @param fileName    The name of the file.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    default T addAttachmentAsSpoiler(InputStream stream, String fileName, String description) {
        return addAttachment(stream, "SPOILER_" + fileName, description);
    }
}
