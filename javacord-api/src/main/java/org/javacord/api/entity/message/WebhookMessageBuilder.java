package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.WebhookMessageBuilderDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.util.DiscordRegexPattern;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

/**
 * This class can help you to create webhook messages.
 */
public class WebhookMessageBuilder {

    /**
     * The message delegate used by this instance.
     */
    protected final WebhookMessageBuilderDelegate delegate = DelegateFactory.createWebhookMessageBuilderDelegate();

    /**
     * Creates a webhook message builder from a message.
     *
     * @param message The message to copy.
     * @return A webhook message builder which would produce the same text as the given message.
     */
    public static WebhookMessageBuilder fromMessage(Message message) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setDisplayAvatar(message.getAuthor().getAvatar())
                .setDisplayName(message.getAuthor().getDisplayName());
        builder.getStringBuilder().append(message.getContent());
        if (!message.getEmbeds().isEmpty()) {
            message.getEmbeds().forEach(embed -> builder.addEmbed(embed.toBuilder()));
        }
        for (MessageAttachment attachment : message.getAttachments()) {
            // Since spoiler status is encoded in the file name, it is copied automatically.
            builder.addAttachment(attachment.getUrl());
        }
        return builder;
    }

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code The code.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder appendCode(String language, String code) {
        delegate.appendCode(language, code);
        return this;
    }

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder append(String message, MessageDecoration... decorations) {
        delegate.append(message, decorations);
        return this;
    }

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder append(Mentionable entity) {
        delegate.append(entity);
        return this;
    }

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    public WebhookMessageBuilder append(Object object) {
        delegate.append(object);
        return this;
    }

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder appendNewLine() {
        delegate.appendNewLine();
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
    public WebhookMessageBuilder setContent(String content) {
        delegate.setContent(content);
        return this;
    }

    /**
     * Adds the embed to the message.
     *
     * @param embed The embed to add.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addEmbed(EmbedBuilder embed) {
        delegate.addEmbed(embed);
        return this;
    }

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addEmbeds(EmbedBuilder... embeds) {
        delegate.addEmbeds(embeds);
        return this;
    }

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder removeEmbed(EmbedBuilder embed) {
        delegate.removeEmbed(embed);
        return this;
    }

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder removeEmbeds(EmbedBuilder... embeds) {
        delegate.removeEmbeds(embeds);
        return this;
    }

    /**
     * Removes all embeds from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder removeAllEmbeds() {
        delegate.removeAllEmbeds();
        return this;
    }

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setTts(boolean tts) {
        delegate.setTts(tts);
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
    public WebhookMessageBuilder addFile(BufferedImage image, String fileName) {
        delegate.addFile(image, fileName);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(File)
     */
    public WebhookMessageBuilder addFile(File file) {
        delegate.addFile(file);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(Icon)
     */
    public WebhookMessageBuilder addFile(Icon icon) {
        delegate.addFile(icon);
        return this;
    }

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    public WebhookMessageBuilder addFile(URL url) {
        delegate.addFile(url);
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
    public WebhookMessageBuilder addFile(byte[] bytes, String fileName) {
        delegate.addFile(bytes, fileName);
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
    public WebhookMessageBuilder addFile(InputStream stream, String fileName) {
        delegate.addFile(stream, fileName);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(BufferedImage, String)
     */
    public WebhookMessageBuilder addFileAsSpoiler(BufferedImage image, String fileName) {
        delegate.addFile(image, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(File)
     */
    public WebhookMessageBuilder addFileAsSpoiler(File file) {
        delegate.addFileAsSpoiler(file);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(Icon)
     */
    public WebhookMessageBuilder addFileAsSpoiler(Icon icon) {
        delegate.addFileAsSpoiler(icon);
        return this;
    }

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    public WebhookMessageBuilder addFileAsSpoiler(URL url) {
        delegate.addFileAsSpoiler(url);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(byte[], String)
     */
    public WebhookMessageBuilder addFileAsSpoiler(byte[] bytes, String fileName) {
        delegate.addFile(bytes, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    public WebhookMessageBuilder addFileAsSpoiler(InputStream stream, String fileName) {
        delegate.addFile(stream, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(BufferedImage image, String fileName) {
        delegate.addAttachment(image, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(File file) {
        delegate.addAttachment(file);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(Icon icon) {
        delegate.addAttachment(icon);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(URL url) {
        delegate.addAttachment(url);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(InputStream stream, String fileName) {
        delegate.addAttachment(stream, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        delegate.addAttachment(image, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(File file) {
        delegate.addAttachmentAsSpoiler(file);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(Icon icon) {
        delegate.addAttachmentAsSpoiler(icon);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(URL url) {
        delegate.addAttachmentAsSpoiler(url);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName) {
        delegate.addAttachment(stream, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Controls who will be mentioned if mentions exist in the message.
     *
     * @param allowedMentions The mention object.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return this;
    }

    /**
     * Sets the display name of the webhook.
     *
     * @param displayName The display name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setDisplayName(String displayName) {
        delegate.setDisplayName(displayName);
        return this;
    }

    /**
     * Sets the display avatar of the webhook.
     *
     * @param avatarUrl The display avatar of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setDisplayAvatar(URL avatarUrl) {
        delegate.setDisplayAvatar(avatarUrl);
        return this;
    }

    /**
     * Sets the display avatar of the webhook.
     *
     * @param avatar The display avatar of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setDisplayAvatar(Icon avatar) {
        delegate.setDisplayAvatar(avatar);
        return this;
    }

    /**
     * Sets the display avatar of the webhook.
     *
     * @param author The author to take display name and display avatar of.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setDisplayAuthor(MessageAuthor author) {
        delegate.setDisplayAuthor(author);
        return this;
    }

    /**
     * Sets the display avatar of the webhook.
     *
     * @param author The author to take display name and display avatar of.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder setDisplayAuthor(User author) {
        delegate.setDisplayAuthor(author);
        return this;
    }

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    public StringBuilder getStringBuilder() {
        return delegate.getStringBuilder();
    }


    /**
     * Sends the message.
     *
     * @param webhook The webhook from which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(IncomingWebhook webhook) {
        return delegate.send(webhook);
    }

    /**
     * Sends the message.
     *
     * @param api The api instance needed to send and return the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(DiscordApi api, long webhookId, String webhookToken) {
        return delegate.send(api, Long.toUnsignedString(webhookId), webhookToken);
    }

    /**
     * Sends the message.
     *
     * @param api The api instance needed to send and return the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(DiscordApi api, String webhookId, String webhookToken) {
        return delegate.send(api, webhookId, webhookToken);
    }

    /**
     * Sends the message.
     *
     * @param api The api instance needed to send the message.
     * @param webhookUrl The url of the webhook from which the message should be sent.
     * @return The sent message.
     * @throws IllegalArgumentException If the link isn't valid.
     */
    public CompletableFuture<Message> send(DiscordApi api, String webhookUrl) throws IllegalArgumentException {
        Matcher matcher = DiscordRegexPattern.WEBHOOK_URL.matcher(webhookUrl);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("The webhook url has an invalid format");
        }

        return send(api, matcher.group("id"), matcher.group("token"));
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param webhook The webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether or not sending the request to discord was successful.
     */
    public CompletableFuture<Void> sendSilently(IncomingWebhook webhook) {
        return delegate.sendSilently(webhook);
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api The api instance needed to send the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether or not sending the request to discord was successful.
     */
    public CompletableFuture<Void> sendSilently(DiscordApi api, long webhookId, String webhookToken) {
        return delegate.sendSilently(api, Long.toUnsignedString(webhookId), webhookToken);
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api The api instance needed to send the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether or not sending the request to discord was successful.
     */
    public CompletableFuture<Void> sendSilently(DiscordApi api, String webhookId, String webhookToken) {
        return delegate.sendSilently(api, webhookId, webhookToken);
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api The api instance needed to send the message.
     * @param webhookUrl The url of the webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether or not sending the request to discord was successful.
     * @throws IllegalArgumentException If the link isn't valid.
     */
    public CompletableFuture<Void> sendSilently(DiscordApi api, String webhookUrl) throws IllegalArgumentException {
        Matcher matcher = DiscordRegexPattern.WEBHOOK_URL.matcher(webhookUrl);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("The webhook url has an invalid format");
        }

        return sendSilently(api, matcher.group("id"), matcher.group("token"));
    }
}
