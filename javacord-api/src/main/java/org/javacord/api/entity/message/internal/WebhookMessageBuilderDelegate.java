package org.javacord.api.entity.message.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.WebhookMessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link WebhookMessageBuilder} to create messages.
 * You usually don't want to interact with this object.
 */
public interface WebhookMessageBuilderDelegate extends MessageBuilderDelegate {

    /**
     * Adds the embed to the message.
     *
     * @param embed The embed to add.
     */
    void addEmbed(EmbedBuilder embed);

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     */
    void addEmbeds(EmbedBuilder... embeds);

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     */
    void removeEmbed(EmbedBuilder embed);

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     */
    void removeEmbeds(EmbedBuilder... embeds);

    /**
     * Removes all embeds from the message.
     */
    void removeAllEmbeds();

    /**
     * Sets the display name of the webhook.
     *
     * @param displayName The display name of the webhook.
     */
    void setDisplayName(String displayName);

    /**
     * Sets the display avatar of the webhook.
     *
     * @param avatarUrl The display avatar of the webhook.
     */
    void setDisplayAvatar(URL avatarUrl);

    /**
     * Sets the display avatar of the webhook.
     *
     * @param avatar The display avatar of the webhook.
     */
    void setDisplayAvatar(Icon avatar);

    /**
     * Sets the display avatar of the webhook.
     *
     * @param author The author to take display name and display avatar of.
     */
    default void setDisplayAuthor(MessageAuthor author) {
        setDisplayAvatar(author.getAvatar());
        setDisplayName(author.getDisplayName());
    }

    /**
     * Sets the display avatar of the webhook.
     *
     * @param author The author to take display name and display avatar of.
     */
    default void setDisplayAuthor(User author) {
        setDisplayAvatar(author.getAvatar());
        setDisplayName(author.getName());
    }

    /**
     * Sends the message without waiting for a response.
     *
     * @param api The api instance needed to send and return the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     *
     * @return The sent message.
     */
    CompletableFuture<Message> send(DiscordApi api, String webhookId, String webhookToken);

    /**
     * Sends the message without waiting for a response.
     *
     * @param webhook The webhook from which the message should be sent.
     *
     * @return A CompletableFuture indicating whether or not sending the request to discord was successful.
     */
    CompletableFuture<Void> sendSilently(IncomingWebhook webhook);

    /**
     * Sends the message without waiting for a response.
     *
     * @param api The api instance needed to send the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     *
     * @return A CompletableFuture indicating whether or not sending the request to discord was successful.
     */
    CompletableFuture<Void> sendSilently(DiscordApi api, String webhookId, String webhookToken);
}
