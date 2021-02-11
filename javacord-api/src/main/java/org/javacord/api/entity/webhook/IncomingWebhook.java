package org.javacord.api.entity.webhook;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.WebhookMessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a webhook you can send messages to.
 */
public interface IncomingWebhook extends Webhook, Messageable {

    @Override
    default boolean isIncomingWebhook() {
        return true;
    }

    @Override
    default boolean isChannelFollowerWebhook() {
        return false;
    }

    /**
     * Gets the secure token of the webhook.
     *
     * @return The secure token of the webhook.
     */
    String getToken();

    /**
     * Gets the secure url of the webhook.
     *
     * @return The secure url of the webhook.
     */
    default URL getUrl() {
        try {
            return new URL(String.format("https://discord.com/api/webhooks/%s/%s", getIdAsString(), getToken()));
        } catch (MalformedURLException e) {
            // This should never ever happen
            return null;
        }
    }

    @Override
    default CompletableFuture<Webhook> getLatestInstance() {
        return getLatestInstanceAsIncomingWebhook()
                .thenApply(incomingWebhook -> incomingWebhook);
    }

    /**
     * Gets an updated instance of this entity from Discord directly.
     *
     * @see IncomingWebhook#getLatestInstance()
     *
     * @return The current instance.
     */
    default CompletableFuture<IncomingWebhook> getLatestInstanceAsIncomingWebhook() {
        return getApi().getIncomingWebhookByIdAndToken(getId(), getToken());
    }

    /**
     * Sends a message.
     *
     * @param embeds The embeds which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder... embeds) {
        return new WebhookMessageBuilder()
                .addEmbeds(embeds)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embeds The embeds which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder... embeds) {
        return new WebhookMessageBuilder()
                .append(content == null ? "" : content)
                .addEmbeds(embeds)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param displayName The name which should be used by the webhook.
     * @param avatarUrl The avatar which should be used by the webhook.
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed,
                                                   String displayName, URL avatarUrl) {
        return new WebhookMessageBuilder()
                .append(content == null ? "" : content)
                .addEmbed(embed)
                .setDisplayName(displayName)
                .setDisplayAvatar(avatarUrl)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param displayName The name which should be used by the webhook.
     * @param avatar The avatar which should be used by the webhook.
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed,
                                                   String displayName, Icon avatar) {
        return new WebhookMessageBuilder()
                .append(content == null ? "" : content)
                .addEmbed(embed)
                .setDisplayName(displayName)
                .setDisplayAvatar(avatar)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param displayName The name which should be used by the webhook.
     * @param avatarUrl The avatar which should be used by the webhook.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, String displayName, URL avatarUrl) {
        return new WebhookMessageBuilder()
                .append(content == null ? "" : content)
                .setDisplayName(displayName)
                .setDisplayAvatar(avatarUrl)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param displayName The name which should be used by the webhook.
     * @param avatar The avatar which should be used by the webhook.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content, String displayName, Icon avatar) {
        return new WebhookMessageBuilder()
                .append(content == null ? "" : content)
                .setDisplayName(displayName)
                .setDisplayAvatar(avatar)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param displayName The name which should be used by the webhook.
     * @param avatarUrl The avatar which should be used by the webhook.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, String displayName, URL avatarUrl) {
        return new WebhookMessageBuilder()
                .addEmbed(embed)
                .setDisplayName(displayName)
                .setDisplayAvatar(avatarUrl)
                .send(this);
    }

    /**
     * Sends a message.
     *
     * @param embed The embed which should be displayed.
     * @param displayName The name which should be used by the webhook.
     * @param avatar The avatar which should be used by the webhook.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed, String displayName, Icon avatar) {
        return new WebhookMessageBuilder()
                .addEmbed(embed)
                .setDisplayName(displayName)
                .setDisplayAvatar(avatar)
                .send(this);
    }
}
