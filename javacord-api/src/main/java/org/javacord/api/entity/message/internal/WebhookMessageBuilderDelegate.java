package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.WebhookMessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.webhook.IncomingWebhook;

import java.net.URL;

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
     * Sends the message without waiting for a response.
     *
     * @param webhook The webhook from which the message should be sent.
     */
    void sendSilently(IncomingWebhook webhook);
}
