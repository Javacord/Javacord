package org.javacord.api.entity.webhook;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.webhook.internal.WebhookBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to create webhooks.
 */
public class WebhookBuilder {

    /**
     * The webhook delegate used by this instance.
     */
    private final WebhookBuilderDelegate delegate;

    /**
     * Creates a new webhook builder.
     *
     * @param channel The server text channel of the webhook.
     */
    public WebhookBuilder(ServerTextChannel channel) {
        delegate = DelegateFactory.createWebhookBuilderDelegate(channel);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(BufferedImage avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(BufferedImage avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(File avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(Icon avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(URL avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(byte[] avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(byte[] avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(InputStream avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(InputStream avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Creates the webhook.
     *
     * @return The created webhook.
     */
    public CompletableFuture<IncomingWebhook> create() {
        return delegate.create();
    }

}
