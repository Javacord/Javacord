package org.javacord.api.entity.webhook.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.WebhookBuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link WebhookBuilder} to create webhooks.
 * You usually don't want to interact with this object.
 */
public interface WebhookBuilderDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name.
     *
     * @param name The new name of the webhook.
     */
    void setName(String name);

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(BufferedImage avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(BufferedImage avatar, String fileType);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(File avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(Icon avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(URL avatar);

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(byte[] avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(byte[] avatar, String fileType);

    /**
     * Sets the avatar.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(InputStream avatar);

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(InputStream avatar, String fileType);

    /**
     * Creates the webhook.
     *
     * @return The created webhook.
     */
    CompletableFuture<IncomingWebhook> create();
}
