package de.btobastian.javacord.entities;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

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
    String getToken();

    /**
     * Deletes the webhook.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), HttpMethod.DELETE, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(res -> null);
    }

    /**
     * Updates the name of the webhook.
     *
     * @param name The new name of the webhook.
     * @return The updated webhook. The current object won't get updated!
     */
    default CompletableFuture<Webhook> updateName(String name) {
        return new RestRequest<Webhook>(getApi(), HttpMethod.PATCH, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString())
                .setBody(new JSONObject().put("name", name))
                .execute(res -> new ImplWebhook(getApi(), res.getBody().getObject()));
    }

    /**
     * Updates the channel of the webhook.
     *
     * @param channel The new channel of the webhook.
     * @return The updated webhook. The current object won't get updated!
     */
    default CompletableFuture<Webhook> updateChannel(ServerTextChannel channel) {
        return new RestRequest<Webhook>(getApi(), HttpMethod.PATCH, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString())
                .setBody(new JSONObject().put("channel_id", channel.getId()))
                .execute(res -> new ImplWebhook(getApi(), res.getBody().getObject()));
    }

}
