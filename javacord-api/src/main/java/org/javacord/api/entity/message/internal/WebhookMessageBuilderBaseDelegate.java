package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.message.embed.EmbedBuilder;

/**
 * You usually don't want to interact with this object.
 */
public interface WebhookMessageBuilderBaseDelegate extends MessageBuilderDelegate {

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

}
