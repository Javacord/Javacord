package de.btobastian.javacord.entity.message;

import de.btobastian.javacord.entity.message.embed.EmbedBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which can receive messages.
 */
public interface Messageable {

    /**
     * Creates a new message builder for this entity.
     *
     * @return The newly created message builder.
     */
    MessageBuilder createMessageBuilder();

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param tts Whether the message should be "text to speech" or not.
     * @param nonce The nonce of the message.
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, InputStream stream, String fileName) {
        return createMessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce)
                .addAttachment(stream, fileName)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param tts Whether the message should be "text to speech" or not.
     * @param nonce The nonce of the message.
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, File... files) {
        MessageBuilder messageBuilder = createMessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param tts Whether the message should be "text to speech" or not.
     * @param nonce The nonce of the message.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce) {
        return createMessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed) {
        return createMessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content) {
        return createMessageBuilder()
                .append(content == null ? "" : content)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed) {
        return createMessageBuilder()
                .setEmbed(embed)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(File... files) {
        MessageBuilder messageBuilder = createMessageBuilder();
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send();
    }

    /**
     * Sends a message.
     *
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(InputStream stream, String fileName) {
        return createMessageBuilder()
                .addAttachment(stream, fileName)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, File... files) {
        MessageBuilder messageBuilder = createMessageBuilder()
                .append(content == null ? "" : content);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, InputStream stream, String fileName) {
        return createMessageBuilder()
                .append(content == null ? "" : content)
                .addAttachment(stream, fileName)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, File... files) {
        MessageBuilder messageBuilder = createMessageBuilder()
                .setEmbed(embed);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send();
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, InputStream stream, String fileName) {
        return createMessageBuilder()
                .setEmbed(embed)
                .addAttachment(stream, fileName)
                .send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, File... files) {
        MessageBuilder messageBuilder = createMessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send();
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, InputStream stream, String fileName) {
        return createMessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .addAttachment(stream, fileName)
                .send();
    }

}
