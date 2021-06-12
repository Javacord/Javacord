package org.javacord.api.interaction.callback;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.InteractionBase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public interface ExtendedInteractionMessageBuilderBase<T> extends InteractionMessageBuilderBase<T> {

    /**
     * Copy a message's values into this build instance.
     *
     * @param message The message to copy.
     * @return The current instance in order to chain call methods.
     */
    T copy(Message message);

    /**
     * Copy an interaction's message.
     *
     * @param interaction The interaction to copy.
     * @return The current instance in order to chain call methods/
     */
    T copy(InteractionBase interaction);

    /**
     * Adds a file to the message.
     *
     * @param image    The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(BufferedImage, String)
     */
    T addFile(BufferedImage image, String fileName);

    /**
     * Adds a file to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(File)
     */
    T addFile(File file);

    /**
     * Adds a file to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(Icon)
     */
    T addFile(Icon icon);

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    T addFile(URL url);

    /**
     * Adds a file to the message.
     *
     * @param bytes    The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(byte[], String)
     */
    T addFile(byte[] bytes, String fileName);

    /**
     * Adds a file to the message.
     *
     * @param stream   The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    T addFile(InputStream stream, String fileName);

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param image    The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(BufferedImage, String)
     */
    T addFileAsSpoiler(BufferedImage image, String fileName);

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(File)
     */
    T addFileAsSpoiler(File file);

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(Icon)
     */
    T addFileAsSpoiler(Icon icon);

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    T addFileAsSpoiler(URL url);

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param bytes    The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(byte[], String)
     */
    T addFileAsSpoiler(byte[] bytes, String fileName);

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param stream   The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    T addFileAsSpoiler(InputStream stream, String fileName);

    /**
     * Adds an attachment to the message.
     *
     * @param image    The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    T addAttachment(BufferedImage image, String fileName);

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    T addAttachment(File file);

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    T addAttachment(Icon icon);

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    T addAttachment(URL url);

    /**
     * Adds an attachment to the message.
     *
     * @param bytes    The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    T addAttachment(byte[] bytes, String fileName);

    /**
     * Adds an attachment to the message.
     *
     * @param stream   The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    T addAttachment(InputStream stream, String fileName);

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image    The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    T addAttachmentAsSpoiler(BufferedImage image, String fileName);

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    T addAttachmentAsSpoiler(File file);

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    T addAttachmentAsSpoiler(Icon icon);

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    T addAttachmentAsSpoiler(URL url);

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes    The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    T addAttachmentAsSpoiler(byte[] bytes, String fileName);

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream   The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    T addAttachmentAsSpoiler(InputStream stream, String fileName);
}
