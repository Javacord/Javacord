package de.btobastian.javacord.entities;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.utils.FileContainer;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to create webhooks.
 */
public class WebhookBuilder {

    /**
     * The channel for the webhook.
     */
    protected final ServerTextChannel channel;

    /**
     * The reason for the creation.
     */
    private String reason = null;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * The avatar to update.
     */
    private FileContainer avatar = null;

    /**
     * Creates a new webhook builder.
     *
     * @param channel The channel for the webhook.
     */
    public WebhookBuilder(ServerTextChannel channel) {
        this.channel = channel;
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Sets the name.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setName(String name) {
        this.name = name;
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(File avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(Icon avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookBuilder setAvatar(URL avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    /**
     * Creates the webhook.
     *
     * @return The created webhook.
     */
    public CompletableFuture<Webhook> create() {
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
        }
        if (avatar != null) {
            return avatar.asByteArray(channel.getApi()).thenAccept(bytes -> {
                String base64Avatar = "data:image/" + avatar.getFileType() + ";base64," +
                        Base64.getEncoder().encodeToString(bytes);
                body.put("avatar", base64Avatar);
            }).thenCompose(aVoid ->
                    new RestRequest<Webhook>(channel.getApi(), RestMethod.POST, RestEndpoint.CHANNEL_WEBHOOK)
                            .setUrlParameters(channel.getIdAsString())
                            .setBody(body)
                            .setAuditLogReason(reason)
                            .execute(result -> new ImplWebhook(channel.getApi(), result.getJsonBody())));
        }
        return new RestRequest<Webhook>(channel.getApi(), RestMethod.POST, RestEndpoint.CHANNEL_WEBHOOK)
                .setUrlParameters(channel.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> new ImplWebhook(channel.getApi(), result.getJsonBody()));
    }

}
