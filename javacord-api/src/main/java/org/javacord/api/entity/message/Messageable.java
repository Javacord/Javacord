package org.javacord.api.entity.message;

import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which can receive messages.
 */
public interface Messageable {

    /**
     * Sends a message.
     *
     * @param content  The content of the message.
     * @param embed    The embed which should be displayed.
     * @param tts      Whether the message should be "text to speech" or not.
     * @param nonce    The nonce of the message.
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, InputStream stream, String fileName) {
        return sendMessage(content, embed, tts, nonce, stream, fileName, null);
    }

    /**
     * Sends a message.
     *
     * @param content         The content of the message.
     * @param embed           The embed which should be displayed.
     * @param tts             Whether the message should be "text to speech" or not.
     * @param nonce           The nonce of the message.
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, InputStream stream, String fileName,
            String fileDescription) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .setTts(tts)
                .setNonce(nonce)
                .addAttachment(stream, fileName, fileDescription)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed   The embed which should be displayed.
     * @param tts     Whether the message should be "text to speech" or not.
     * @param nonce   The nonce of the message.
     * @param files   The file(s) to send.
     * @return The message that has been sent.
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
     * @param embed   The embed which should be displayed.
     * @param tts     Whether the message should be "text to speech" or not.
     * @param nonce   The nonce of the message.
     * @return The message that has been sent.
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
     * @param embed   The embed of the message.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .addEmbed(embed)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content    The content of the message.
     * @param embed      The embed which should be displayed.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content,
                                                   EmbedBuilder embed,
                                                   HighLevelComponent... components) {
        return sendMessage(content, Collections.singletonList(embed), components);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed   The embed which should be displayed.
     * @param files   The file(s) to send.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, File... files) {
        return sendMessage(content, Collections.singletonList(embed), files);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embeds  A list of embeds which should be displayed.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, List<EmbedBuilder> embeds) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbeds(embeds)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content    The content of the message.
     * @param embeds     A list of embeds which should be displayed.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content,
                                                   List<EmbedBuilder> embeds,
                                                   HighLevelComponent... components) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbeds(embeds)
                .addComponents(components)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embeds  A list of embeds which should be displayed.
     * @param files   The file(s) to send.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, List<EmbedBuilder> embeds, File... files) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbeds(embeds);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embeds  An array of the new embeds of the message.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder... embeds) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .addEmbeds(embeds)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content    The content of the message.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The message that has been sent.
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
     * @param files   The file(s) to send.
     * @return The message that has been sent.
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
     * @param files The file(s) to send.
     * @return The message that has been sent.
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
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(InputStream stream, String fileName) {
        return sendMessage(stream, fileName, null);
    }

    /**
     * Sends a message.
     *
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(InputStream stream, String fileName, String fileDescription) {
        return new MessageBuilder()
                .addAttachment(stream, fileName, fileDescription)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content  The content of the message.
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, InputStream stream, String fileName) {
        return sendMessage(content, stream, fileName, null);
    }

    /**
     * Sends a message.
     *
     * @param content         The content of the message.
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, InputStream stream, String fileName,
                                                   String fileDescription) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .addAttachment(stream, fileName, fileDescription)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content  The content of the message.
     * @param embed    The embed which should be displayed.
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, InputStream stream,
                                                   String fileName) {
        return sendMessage(content, embed, stream, fileName, null);
    }

    /**
     * Sends a message.
     *
     * @param content         The content of the message.
     * @param embed           The embed which should be displayed.
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, InputStream stream,
                                                   String fileName, String fileDescription) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbed(embed)
                .addAttachment(stream, fileName, fileDescription)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content  The content of the message.
     * @param embeds   A list of embeds which should be displayed.
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, List<EmbedBuilder> embeds, InputStream stream,
                                                   String fileName) {
        return sendMessage(content, embeds, stream, fileName, null);
    }

    /**
     * Sends a message.
     *
     * @param content         The content of the message.
     * @param embeds          A list of embeds which should be displayed.
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(String content, List<EmbedBuilder> embeds, InputStream stream,
                                                   String fileName, String fileDescription) {
        return new MessageBuilder()
                .append(content == null ? "" : content)
                .setEmbeds(embeds)
                .addAttachment(stream, fileName, fileDescription)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embed The new embed of the message.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed) {
        return sendMessage(Collections.singletonList(embed));
    }

    /**
     * Sends a message.
     *
     * @param embeds An array of the new embeds of the message.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder... embeds) {
        return sendMessage(Arrays.asList(embeds));
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param files The file(s) to send.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, File... files) {
        return sendMessage(Collections.singletonList(embed), files);
    }

    /**
     * Sends a message.
     *
     * @param embed      The embed which should be displayed.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, HighLevelComponent... components) {
        return sendMessage(Collections.singletonList(embed), components);
    }

    /**
     * Sends a message.
     *
     * @param embed    The embed which should be displayed.
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, InputStream stream, String fileName) {
        return sendMessage(Collections.singletonList(embed), stream, fileName);
    }

    /**
     * Sends a message.
     *
     * @param embed           The embed which should be displayed.
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, InputStream stream, String fileName,
                                                   String fileDescription) {
        return sendMessage(Collections.singletonList(embed), stream, fileName, fileDescription);
    }

    /**
     * Sends a message.
     *
     * @param embeds A list of embeds which should be displayed.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(List<EmbedBuilder> embeds) {
        return new MessageBuilder()
                .setEmbeds(embeds)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embeds     A list of embeds which should be displayed.
     * @param components High level components to add to the message, most probably of type ActionRow.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(List<EmbedBuilder> embeds, HighLevelComponent... components) {
        return new MessageBuilder()
                .setEmbeds(embeds)
                .addComponents(components)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embeds A list of embeds which should be displayed.
     * @param files  The file(s) to send.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(List<EmbedBuilder> embeds, File... files) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbeds(embeds);
        for (File file : files) {
            messageBuilder.addAttachment(file);
        }
        return messageBuilder.send(this);
    }

    /**
     * Sends a message.
     *
     * @param embeds   A list of embeds which should be displayed.
     * @param stream   The stream for the file to send.
     * @param fileName The name of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(List<EmbedBuilder> embeds, InputStream stream, String fileName) {
        return new MessageBuilder()
                .setEmbeds(embeds)
                .addAttachment(stream, fileName)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embeds          A list of embeds which should be displayed.
     * @param stream          The stream for the file to send.
     * @param fileName        The name of the file.
     * @param fileDescription The description of the file.
     * @return The message that has been sent.
     */
    default CompletableFuture<Message> sendMessage(List<EmbedBuilder> embeds, InputStream stream, String fileName,
                                                   String fileDescription) {
        return new MessageBuilder()
                .setEmbeds(embeds)
                .addAttachment(stream, fileName, fileDescription)
                .send(this);
    }
}
