package org.javacord.api.entity.webhook;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Updatable;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.webhook.WebhookAttachableListenerManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a webhook.
 * Webhook objects won't receive any updates!
 */
public interface Webhook extends DiscordEntity, Updatable<Webhook>, WebhookAttachableListenerManager {

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
        return delete(null);
    }

    /**
     * Deletes the webhook.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(String reason);

    /**
     * Gets the updater for this webhook.
     *
     * @return The updater for this webhook.
     */
    default WebhookUpdater createUpdater() {
        return new WebhookUpdater(this);
    }

    /**
     * Updates the name of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the webhook.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    /**
     * Updates the channel of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param channel The new channel of the webhook.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateChannel(ServerTextChannel channel) {
        return createUpdater().setChannel(channel).update();
    }

    /**
     * Updates the avatar of the webhook.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(BufferedImage avatar) {
        return createUpdater().setAvatar(avatar).update();
    }

    /**
     * Updates the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(BufferedImage avatar, String fileType) {
        return createUpdater().setAvatar(avatar, fileType).update();
    }

    /**
     * Updates the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(File avatar) {
        return createUpdater().setAvatar(avatar).update();
    }

    /**
     * Updates the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(Icon avatar) {
        return createUpdater().setAvatar(avatar).update();
    }

    /**
     * Updates the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(URL avatar) {
        return createUpdater().setAvatar(avatar).update();
    }

    /**
     * Updates the avatar of the webhook.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(byte[] avatar) {
        return createUpdater().setAvatar(avatar).update();
    }

    /**
     * Updates the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(byte[] avatar, String fileType) {
        return createUpdater().setAvatar(avatar, fileType).update();
    }

    /**
     * Updates the avatar of the webhook.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(InputStream avatar) {
        return createUpdater().setAvatar(avatar).update();
    }

    /**
     * Updates the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} ()} which provides a better performance!
     *
     * @param avatar The new avatar.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> updateAvatar(InputStream avatar, String fileType) {
        return createUpdater().setAvatar(avatar, fileType).update();
    }

    /**
     * Removes the avatar of the webhook.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link WebhookUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return The updated webhook.
     */
    default CompletableFuture<Webhook> removeAvatar() {
        return createUpdater().removeAvatar().update();
    }

    @Override
    default CompletableFuture<Webhook> getLatestInstance() {
        return getApi().getWebhookById(getId());
    }

}
