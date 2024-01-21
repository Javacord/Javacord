package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.internal.MessageBuilderAttachment;
import org.javacord.api.entity.message.internal.MessageBuilderBaseDelegate;
import org.javacord.api.entity.message.internal.MessageBuilderComponent;
import org.javacord.api.entity.message.internal.MessageBuilderContent;
import org.javacord.api.entity.message.internal.MessageBuilderEmbed;
import org.javacord.api.entity.message.internal.MessageBuilderMiscellaneous;
import org.javacord.api.entity.message.internal.WebhookMessageBuilderDelegate;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.exception.MissingIntentException;
import org.javacord.api.util.DiscordRegexPattern;
import org.javacord.api.util.internal.DelegateFactory;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

/**
 * This class can help you to create webhook messages.
 */
public class WebhookMessageBuilder implements MessageBuilderContent<WebhookMessageBuilder>,
        MessageBuilderComponent<WebhookMessageBuilder>,
        MessageBuilderAttachment<WebhookMessageBuilder>,
        MessageBuilderEmbed<WebhookMessageBuilder>,
        MessageBuilderMiscellaneous<WebhookMessageBuilder> {

    /**
     * The message delegate used by this instance.
     */
    protected final WebhookMessageBuilderDelegate delegate = DelegateFactory.createWebhookMessageBuilderDelegate();

    @Override
    public MessageBuilderBaseDelegate getDelegate() {
        return delegate;
    }

    /**
     * Creates a webhook message builder from a message.
     *
     * @param message The message to copy.
     * @return A webhook message builder which would produce the same text as the given message.
     * @throws MissingIntentException See Javadoc of {@link Message#getContent()} for further explanation.
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
     * @param api          The api instance needed to send and return the message.
     * @param webhookId    The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(DiscordApi api, long webhookId, String webhookToken) {
        return delegate.send(api, Long.toUnsignedString(webhookId), webhookToken);
    }

    /**
     * Sends the message.
     *
     * @param api          The api instance needed to send and return the message.
     * @param webhookId    The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(DiscordApi api, String webhookId, String webhookToken) {
        return delegate.send(api, webhookId, webhookToken);
    }

    /**
     * Sends the message.
     *
     * @param api        The api instance needed to send the message.
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
     * @param api          The api instance needed to send the message.
     * @param webhookId    The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether sending the request to discord was successful.
     */
    public CompletableFuture<Void> sendSilently(DiscordApi api, long webhookId, String webhookToken) {
        return delegate.sendSilently(api, Long.toUnsignedString(webhookId), webhookToken);
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api          The api instance needed to send the message.
     * @param webhookId    The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return A CompletableFuture indicating whether sending the request to discord was successful.
     */
    public CompletableFuture<Void> sendSilently(DiscordApi api, String webhookId, String webhookToken) {
        return delegate.sendSilently(api, webhookId, webhookToken);
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api        The api instance needed to send the message.
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
