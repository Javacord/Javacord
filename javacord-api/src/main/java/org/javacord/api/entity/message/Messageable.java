package org.javacord.api.entity.message;

import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which can receive messages.
 */
public interface Messageable {

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
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce)
                .addAttachment(stream, fileName)
                .send(this);
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
        MessageBuilder messageBuilder = new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
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
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content,
                                                   EmbedBuilder embed,
                                                   HighLevelComponent... components) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .addComponents(components)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, HighLevelComponent... components) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .addComponents(components)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, HighLevelComponent... components) {
        return new MessageBuilder()
                .setEmbed(embed)
                .addComponents(components)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed) {
        return new MessageBuilder()
                .setEmbed(embed)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(File... files) {
        MessageBuilder messageBuilder = new MessageBuilder();
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
    }

    /**
     * Sends a message.
     *
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(InputStream stream, String fileName) {
        return new MessageBuilder()
                .addAttachment(stream, fileName)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, File... files) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .append(content == null ? "" : content);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
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
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .addAttachment(stream, fileName)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param files The file(s) to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, File... files) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embed);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
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
        return new MessageBuilder()
                .setEmbed(embed)
                .addAttachment(stream, fileName)
                .send(this);
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
        MessageBuilder messageBuilder = new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
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
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, InputStream stream,
                                                   String fileName) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .addAttachment(stream, fileName)
                .send(this);
    }
}
