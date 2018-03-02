package de.btobastian.javacord.entity.webhook;

import de.btobastian.javacord.entity.Icon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to create webhooks.
 */
public interface WebhookBuilder {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAuditLogReason(String reason);

    /**
     * Sets the name.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setName(String name);

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(BufferedImage avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(BufferedImage avatar, String fileType);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(File avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(Icon avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(URL avatar);

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(byte[] avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(byte[] avatar, String fileType);

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(InputStream avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    WebhookBuilder setAvatar(InputStream avatar, String fileType);

    /**
     * Creates the webhook.
     *
     * @return The created webhook.
     */
    CompletableFuture<Webhook> create();

}
