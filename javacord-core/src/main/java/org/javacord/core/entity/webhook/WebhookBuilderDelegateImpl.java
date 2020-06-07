package org.javacord.core.entity.webhook;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.internal.WebhookBuilderDelegate;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link WebhookBuilderDelegate}.
 */
public class WebhookBuilderDelegateImpl implements WebhookBuilderDelegate {

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
     * Creates a new webhook builder delegate.
     *
     * @param channel The channel for the webhook.
     */
    public WebhookBuilderDelegateImpl(ServerTextChannel channel) {
        this.channel = channel;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAvatar(BufferedImage avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
    }

    @Override
    public void setAvatar(BufferedImage avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
    }

    @Override
    public void setAvatar(File avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
    }

    @Override
    public void setAvatar(Icon avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
    }

    @Override
    public void setAvatar(URL avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
    }

    @Override
    public void setAvatar(byte[] avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
    }

    @Override
    public void setAvatar(byte[] avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
    }

    @Override
    public void setAvatar(InputStream avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
    }

    @Override
    public void setAvatar(InputStream avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
    }

    @Override
    public CompletableFuture<IncomingWebhook> create() {
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("name", name);
        if (avatar != null) {
            return avatar.asByteArray(channel.getApi()).thenAccept(bytes -> {
                String base64Avatar = "data:image/" + avatar.getFileType() + ";base64,"
                        + Base64.getEncoder().encodeToString(bytes);
                body.put("avatar", base64Avatar);
            }).thenCompose(aVoid ->
                    new RestRequest<IncomingWebhook>(channel.getApi(), RestMethod.POST, RestEndpoint.CHANNEL_WEBHOOK)
                            .setUrlParameters(channel.getIdAsString())
                            .setBody(body)
                            .setAuditLogReason(reason)
                            .execute(result -> new IncomingWebhookImpl(channel.getApi(), result.getJsonBody())));
        }
        return new RestRequest<IncomingWebhook>(channel.getApi(), RestMethod.POST, RestEndpoint.CHANNEL_WEBHOOK)
                .setUrlParameters(channel.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> new IncomingWebhookImpl(channel.getApi(), result.getJsonBody()));
    }

}
