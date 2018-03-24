package org.javacord.api.entity.webhook;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.webhook.internal.WebhookUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update webhooks.
 */
public class WebhookUpdater {

    /**
     * The webhook delegate used by this instance.
     */
    private final WebhookUpdaterDelegate delegate;

    /**
     * Creates a new webhook updater.
     *
     * @param webhook The webhook to update.
     */
    public WebhookUpdater(Webhook webhook) {
        delegate = DelegateFactory.createWebhookUpdaterDelegate(webhook);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Queues the channel to be updated.
     *
     * @param channel The new channel of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setChannel(ServerTextChannel channel) {
        delegate.setChannel(channel);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(BufferedImage avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(BufferedImage avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(File avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(Icon avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(URL avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(byte[] avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(byte[] avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(InputStream avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(InputStream avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Queues the avatar to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater removeAvatar() {
        delegate.removeAvatar();
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return The updated webhook or the current instance if no updates were queued.
     */
    public CompletableFuture<Webhook> update() {
        return delegate.update();
    }

}
