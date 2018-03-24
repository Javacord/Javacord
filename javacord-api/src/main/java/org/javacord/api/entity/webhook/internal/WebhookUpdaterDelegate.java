package org.javacord.api.entity.webhook.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.entity.webhook.WebhookUpdater;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link WebhookUpdater} to update webhooks.
 * You usually don't want to interact with this object.
 */
public interface WebhookUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the webhook.
     */
    void setName(String name);

    /**
     * Queues the channel to be updated.
     *
     * @param channel The new channel of the webhook.
     */
    void setChannel(ServerTextChannel channel);

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(BufferedImage avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(BufferedImage avatar, String fileType);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(File avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(Icon avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(URL avatar);

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(byte[] avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(byte[] avatar, String fileType);

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(InputStream avatar);

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(InputStream avatar, String fileType);

    /**
     * Queues the avatar to be removed.
     */
    void removeAvatar();

    /**
     * Performs the queued updates.
     *
     * @return The updated webhook or the current instance if no updates were queued.
     */
    CompletableFuture<Webhook> update();

}
