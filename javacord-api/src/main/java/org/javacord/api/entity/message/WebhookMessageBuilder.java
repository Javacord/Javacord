package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.component.HighLevelComponent;
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
import java.util.Arrays;
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
            builder.addAttachment(attachment.getUrl(), attachment.getDescription()
                    .orElse(null));
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
     * Adds multiple components to the message.
     *
     * @param components The component builders.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addComponents(HighLevelComponent... components) {
        delegate.addComponents(components);
        return this;
    }

    /**
     * Removes all components from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder removeAllComponents() {
        delegate.removeAllComponents();
        return this;
    }

    /**
     * Remove a component from the message.
     *
     * @param index The index placement to remove from.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder removeComponent(int index) {
        delegate.removeComponent(index);
        return this;
    }

    /**
     * Remove a component from the message.
     *
     * @param builder The component.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder removeComponent(HighLevelComponent builder) {
        delegate.removeComponent(builder);
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
        delegate.addEmbeds(Arrays.asList(embeds));
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
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(BufferedImage image, String fileName) {
        addAttachment(image, fileName, null);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @param description The description of the image.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, fileName,  description);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(File file) {
        addAttachment(file, null);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @param description The description of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(File file, String description) {
        delegate.addAttachment(file, description);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(Icon icon) {
        addAttachment(icon, null);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @param description The description of the icon.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(Icon icon, String description) {
        delegate.addAttachment(icon, description);
        return this;
    }


    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(URL url) {
        addAttachment(url, null);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @param description The description of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(URL url, String description) {
        delegate.addAttachment(url, description);
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
        addAttachment(bytes, fileName, null);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @param description The description of the
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, fileName, description);
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
        addAttachment(stream, fileName, null);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @param description The description of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachment(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, fileName, description);
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
        addAttachment(image, "SPOILER_" + fileName, null);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @param description The description of the image.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, "SPOILER_" + fileName, description);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(File file) {
        addAttachmentAsSpoiler(file, null);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @param description The description of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(File file, String description) {
        delegate.addAttachmentAsSpoiler(file, description);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon, null);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @param description The description of the icon.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(Icon icon, String description) {
        delegate.addAttachmentAsSpoiler(icon, description);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url, null);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @param description The description of the
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(URL url, String description) {
        delegate.addAttachmentAsSpoiler(url, description);
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
        addAttachment(bytes, "SPOILER_" + fileName, null);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @param description The description of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName, description);
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
        addAttachment(stream, "SPOILER_" + fileName, null);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @param description The description of the file.
     * @return The current instance in order to chain call methods.
     */
    public WebhookMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, "SPOILER_" + fileName, description);
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
     * @return A CompletableFuture indicating whether sending the request to discord was successful.
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
     * @return A CompletableFuture indicating whether sending the request to discord was successful.
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
     * @return A CompletableFuture indicating whether sending the request to discord was successful.
     */
    public CompletableFuture<Void> sendSilently(DiscordApi api, String webhookId, String webhookToken) {
        return delegate.sendSilently(api, webhookId, webhookToken);
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api The api instance needed to send the message.
     * @param webhookUrl The url of the webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether sending the request to discord was successful.
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
