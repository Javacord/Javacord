package de.btobastian.javacord.entity.message;

import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.Mentionable;
import de.btobastian.javacord.entity.channel.TextChannel;
import de.btobastian.javacord.entity.message.embed.EmbedBuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can help you to create messages.
 */
public interface MessageBuilder {

    /**
     * Creates a message builder from a message.
     *
     * @param message The message to copy.
     * @return A message builder which would produce the same text as the given message.
     */
    static MessageBuilder fromMessage(Message message) {
        MessageBuilder builder = message.getChannel().createMessageBuilder();
        builder.getStringBuilder().append(message.getContent());
        if (!message.getEmbeds().isEmpty()) {
            builder.setEmbed(message.getEmbeds().get(0).toBuilder());
        }
        for (MessageAttachment attachment : message.getAttachments()) {
            builder.addAttachment(attachment.getUrl());
        }
        return builder;
    }

    /**
     * Sets the channel to which the message should be send.
     *
     * @param channel The channel to set.
     * @return The current instance in order to chain call methods.
     */
    default MessageBuilder setChannel(TextChannel channel) {
        return setReceiver(channel);
    }

    /**
     * Sets the entity which should receive the message.
     *
     * @param messageable The entity to set.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder setReceiver(Messageable messageable);

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder append(String message, MessageDecoration... decorations);

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code The code.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder appendCode(String language, String code);

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder append(Mentionable entity);

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    MessageBuilder append(Object object);

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder appendNewLine();

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder setContent(String content);

    /**
     * Sets the embed of the message.
     *
     * @param embed The embed to set.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder setEmbed(EmbedBuilder embed);

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder setTts(boolean tts);

    /**
     * Adds a file to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(BufferedImage, String)
     */
    MessageBuilder addFile(BufferedImage image, String fileName);

    /**
     * Adds a file to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(File)
     */
    MessageBuilder addFile(File file);

    /**
     * Adds a file to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(Icon)
     */
    MessageBuilder addFile(Icon icon);

    /**
     * Adds a file to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    MessageBuilder addFile(URL url);

    /**
     * Adds a file to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(byte[], String)
     */
    MessageBuilder addFile(byte[] bytes, String fileName);

    /**
     * Adds a file to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    MessageBuilder addFile(InputStream stream, String fileName);

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder addAttachment(BufferedImage image, String fileName);

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder addAttachment(File file);

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder addAttachment(Icon icon);

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder addAttachment(URL url);

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder addAttachment(byte[] bytes, String fileName);

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder addAttachment(InputStream stream, String fileName);

    /**
     * Sets the nonce of the message.
     *
     * @param nonce The nonce to set.
     * @return The current instance in order to chain call methods.
     */
    MessageBuilder setNonce(String nonce);

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    StringBuilder getStringBuilder();

    /**
     * Sends the message.
     *
     * @return The sent message.
     * @throws IllegalStateException If no receiver/channel was set.
     * @see #setChannel(TextChannel)
     * @see #setReceiver(Messageable)
     */
    CompletableFuture<Message> send();

}
