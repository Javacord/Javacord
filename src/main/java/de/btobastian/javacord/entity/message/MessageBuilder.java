package de.btobastian.javacord.entity.message;

import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.Mentionable;
import de.btobastian.javacord.entity.channel.TextChannel;
import de.btobastian.javacord.entity.message.embed.EmbedBuilder;
import de.btobastian.javacord.entity.user.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;

/**
 * This class can help you to create messages.
 */
public class MessageBuilder {

    /**
     * The base factory. It's only used to create new factories.
     */
    private static final MessageFactory baseFactory;

    // Load it static, because it has a better performance to load it only once
    static {
        ServiceLoader<MessageFactory> factoryServiceLoader = ServiceLoader.load(MessageFactory.class);
        Iterator<MessageFactory> factoryIterator = factoryServiceLoader.iterator();
        if (factoryIterator.hasNext()) {
            baseFactory = factoryIterator.next();
            if (factoryIterator.hasNext()) {
                throw new IllegalStateException("Found more than one MessageFactory implementation!");
            }
        } else {
            throw new IllegalStateException("No MessageFactory implementation was found!");
        }
    }

    /**
     * The message factory used by this instance.
     */
    private final MessageFactory factory;

    /**
     * Creates a new message builder.
     */
    public MessageBuilder() {
        factory = baseFactory.getNewInstance();
    }

    /**
     * Creates a message builder from a message.
     *
     * @param message The message to copy.
     * @return A message builder which would produce the same text as the given message.
     */
    static public MessageBuilder fromMessage(Message message) {
        MessageBuilder builder = new MessageBuilder();
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
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder append(String message, MessageDecoration... decorations) {
        factory.append(message, decorations);
        return this;
    }

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code The code.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder appendCode(String language, String code) {
        factory.appendCode(language, code);
        return this;
    }

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder append(Mentionable entity) {
        factory.append(entity);
        return this;
    }

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    public MessageBuilder append(Object object) {
        factory.append(object);
        return this;
    }

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder appendNewLine() {
        factory.appendNewLine();
        return this;
    }

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setContent(String content) {
        factory.setContent(content);
        return this;
    }

    /**
     * Sets the embed of the message.
     *
     * @param embed The embed to set.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setEmbed(EmbedBuilder embed) {
        factory.setEmbed(embed);
        return this;
    }

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setTts(boolean tts) {
        factory.setTts(tts);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(BufferedImage, String)
     */
    public MessageBuilder addFile(BufferedImage image, String fileName) {
        factory.addFile(image, fileName);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(File)
     */
    public MessageBuilder addFile(File file) {
        factory.addFile(file);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(Icon)
     */
    public MessageBuilder addFile(Icon icon) {
        factory.addFile(icon);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    public MessageBuilder addFile(URL url) {
        factory.addFile(url);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(byte[], String)
     */
    public MessageBuilder addFile(byte[] bytes, String fileName) {
        factory.addFile(bytes, fileName);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    public MessageBuilder addFile(InputStream stream, String fileName) {
        factory.addFile(stream, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(BufferedImage image, String fileName) {
        factory.addAttachment(image, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(File file) {
        factory.addAttachment(file);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(Icon icon) {
        factory.addAttachment(icon);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(URL url) {
        factory.addAttachment(url);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(byte[] bytes, String fileName) {
        factory.addAttachment(bytes, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(InputStream stream, String fileName) {
        factory.addAttachment(stream, fileName);
        return this;
    }

    /**
     * Sets the nonce of the message.
     *
     * @param nonce The nonce to set.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setNonce(String nonce) {
        factory.setNonce(nonce);
        return this;
    }

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    public StringBuilder getStringBuilder() {
        return factory.getStringBuilder();
    }

    /**
     * Sends the message.
     *
     * @param user The user to which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(User user) {
        return factory.send(user);
    }

    /**
     * Sends the message.
     *
     * @param channel The channel in which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(TextChannel channel) {
        return factory.send(channel);
    }

    /**
     * Sends the message.
     *
     * @param messageable The receiver of the message.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(Messageable messageable) {
        return factory.send(messageable);
    }

}
