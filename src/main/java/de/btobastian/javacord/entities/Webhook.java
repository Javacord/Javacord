package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.channels.TextChannel;

import java.util.Optional;

/**
 * This class represents a webhook.
 * Webhook objects won't receive any updates!
 */
public interface Webhook extends DiscordEntity, IconHolder {

    /**
     * Gets the server id of the webhook.
     *
     * @return The server id of the webhook.
     */
    Optional<Long> getServerId();

    /**
     * Gets the server of the webhook.
     *
     * @return The server of the webhook.
     */
    Optional<Server> getServer();

    /**
     * Gets the channel id of the webhook.
     *
     * @return The channel id of the webhook.
     */
    long getChannelId();

    /**
     * Gets the channel of the webhook.
     *
     * @return The channel of the webhook.
     */
    Optional<TextChannel> getChannel();

    /**
     * Gets the creator of the webhook.
     *
     * @return The creator of the webhook.
     */
    Optional<User> getCreator();

    /**
     * Gets the default name of the webhook.
     *
     * @return The default name of the webhook.
     */
    Optional<String> getName();

    /**
     * Gets the secure token of the webhook.
     *
     * @return The secure token of the webhook.
     */
    String getToken();

}
