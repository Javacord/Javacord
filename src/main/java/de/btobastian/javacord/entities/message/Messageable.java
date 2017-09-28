package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, InputStream stream, String fileName);

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param tts Whether the message should be "text to speech" or not.
     * @param nonce The nonce of the message.
     * @param file The file to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, File file) {
        try {
            return sendMessage(content, embed, tts, nonce, file == null ? null : new FileInputStream(file),
                    file == null ? null : file.getName());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The provided file couldn't be found!");
        }
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
        return sendMessage(content, embed, tts, nonce, null);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed) {
        return sendMessage(content, embed, false, null, null);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content) {
        return sendMessage(content, null, false, null);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed) {
        return sendMessage(null, embed, false, null);
    }

    /**
     * Sends a message.
     *
     * @param file The file to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(File file) {
        return sendMessage(null, null, false, null, file);
    }

    /**
     * Sends a message.
     *
     * @param stream The stream for the file to send.
     * @param fileName The name of the file.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(InputStream stream, String fileName) {
        return sendMessage(null, null, false, null, stream, fileName);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param file The file to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, File file) {
        return sendMessage(content, null, false, null, file);
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
        return sendMessage(content, null, false, null, stream, fileName);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param file The file to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, File file) {
        return sendMessage(null, embed, false, null, file);
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
        return sendMessage(null, embed, false, null, stream, fileName);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param file The file to send.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, File file) {
        return sendMessage(null, embed, false, null, file);
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
        return sendMessage(null, embed, false, null, stream, fileName);
    }

}
