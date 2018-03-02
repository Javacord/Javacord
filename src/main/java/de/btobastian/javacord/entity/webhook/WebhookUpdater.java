package de.btobastian.javacord.entity.webhook;

import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.channel.ServerTextChannel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update webhooks.
 */
public interface WebhookUpdater {
    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setName(String name);

    /**
     * Queues the channel to be updated.
     *
     * @param channel The new channel of the webhook.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setChannel(ServerTextChannel channel);

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(BufferedImage avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(BufferedImage avatar, String fileType);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(File avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(Icon avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(URL avatar);

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(byte[] avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(byte[] avatar, String fileType);

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(InputStream avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater setAvatar(InputStream avatar, String fileType);

    /**
     * Queues the avatar to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    WebhookUpdater removeAvatar();

    /**
     * Performs the queued updates.
     *
     * @return The updated webhook or the current instance if no updates were queued.
     */
    CompletableFuture<Webhook> update();

}
