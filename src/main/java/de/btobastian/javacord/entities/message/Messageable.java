package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

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
     * @return The sent message.
     */
    CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce);

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
     * Sends a message which contains an embed.
     *
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed) {
        return sendMessage(null, embed, false, null);
    }

}
