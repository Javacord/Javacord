package de.btobastian.javacord.entities;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a webhook.
 * Webhook objects won't receive any updates!
 */
public interface Webhook extends DiscordEntity {

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
     * Gets the default avatar of the webhook.
     *
     * @return The default avatar of the webhook.
     */
    Optional<Icon> getAvatar();

    /**
     * Gets the secure token of the webhook.
     *
     * @return The secure token of the webhook.
     */
    Optional<String> getToken();

    /**
     * Deletes the webhook.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    /**
     * Updates the name of the webhook.
     *
     * @param name The new name of the webhook.
     * @return The updated webhook. The current object won't get updated!
     */
    default CompletableFuture<Webhook> updateName(String name) {
        return new RestRequest<Webhook>(getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString())
                .setBody(JsonNodeFactory.instance.objectNode().put("name", name))
                .execute(result -> new ImplWebhook(getApi(), result.getJsonBody()));
    }

    /**
     * Updates the channel of the webhook.
     *
     * @param channel The new channel of the webhook.
     * @return The updated webhook. The current object won't get updated!
     */
    default CompletableFuture<Webhook> updateChannel(ServerTextChannel channel) {
        return new RestRequest<Webhook>(getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString())
                .setBody(JsonNodeFactory.instance.objectNode().put("channel_id", channel.getId()))
                .execute(result -> new ImplWebhook(getApi(), result.getJsonBody()));
    }

}
