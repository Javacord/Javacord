package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.image.BufferedImage;
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
     * Gets the updater for this webhook.
     *
     * @return The updater for this webhook.
     */
    default WebhookUpdater getUpdater() {
        return new WebhookUpdater(this);
    }

    /**
     * Updates the name of the webhook.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param name The new name of the webhook.
     * @return The updated webhook or the current instance if no updates were queued.
     */
    default CompletableFuture<Webhook> updateName(String name) {
        return getUpdater().setName(name).update();
    }

    /**
     * Updates the channel of the webhook.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param channel The new channel of the webhook.
     * @return The updated webhook or the current instance if no updates were queued.
     */
    default CompletableFuture<Webhook> updateChannel(ServerTextChannel channel) {
        return getUpdater().setChannel(channel).update();
    }

    /**
     * Updates the avatar of the webhook.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param avatar The new avatar of the webhook.
     * @return The updated webhook or the current instance if no updates were queued.
     */
    default CompletableFuture<Webhook> updateAvatar(BufferedImage avatar) {
        return getUpdater().setAvatar(avatar).update();
    }

    /**
     * Removes the avatar of the webhook.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return The updated webhook or the current instance if no updates were queued.
     */
    default CompletableFuture<Webhook> removeAvatar() {
        return getUpdater().removeAvatar().update();
    }

}
