package de.btobastian.javacord.entity.webhook.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.channel.ServerTextChannel;
import de.btobastian.javacord.entity.webhook.Webhook;
import de.btobastian.javacord.entity.webhook.WebhookBuilder;
import de.btobastian.javacord.util.FileContainer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link WebhookBuilder}.
 */
public class ImplWebhookBuilder implements WebhookBuilder {

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
    public ImplWebhookBuilder(ServerTextChannel channel) {
        this.channel = channel;
    }

    @Override
    public ImplWebhookBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ImplWebhookBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(BufferedImage avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(BufferedImage avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(File avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(Icon avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(URL avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(byte[] avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(byte[] avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(InputStream avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        return this;
    }

    @Override
    public ImplWebhookBuilder setAvatar(InputStream avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    @Override
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
