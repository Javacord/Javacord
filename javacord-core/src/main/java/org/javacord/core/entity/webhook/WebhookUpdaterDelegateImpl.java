package org.javacord.core.entity.webhook;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.entity.webhook.internal.WebhookUpdaterDelegate;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link WebhookUpdaterDelegate}.
 */
public class WebhookUpdaterDelegateImpl implements WebhookUpdaterDelegate {

    /**
     * The webhook to update.
     */
    protected final Webhook webhook;

    /**
     * The reason for the update.
     */
    private String reason = null;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * The channel to update.
     */
    protected ServerTextChannel channel = null;

    /**
     * The avatar to update.
     */
    private FileContainer avatar = null;

    /**
     * Whether the avatar should be updated or not.
     */
    protected boolean updateAvatar = false;

    /**
     * Creates a new webhook updater delegate.
     *
     * @param webhook The webhook to update.
     */
    public WebhookUpdaterDelegateImpl(Webhook webhook) {
        this.webhook = webhook;
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
    public void setChannel(ServerTextChannel channel) {
        this.channel = channel;
    }

    @Override
    public void setAvatar(BufferedImage avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        updateAvatar = true;
    }

    @Override
    public void setAvatar(BufferedImage avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        updateAvatar = true;
    }

    @Override
    public void setAvatar(File avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        updateAvatar = true;
    }

    @Override
    public void setAvatar(Icon avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        updateAvatar = true;
    }

    @Override
    public void setAvatar(URL avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        updateAvatar = true;
    }

    @Override
    public void setAvatar(byte[] avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        updateAvatar = true;
    }

    @Override
    public void setAvatar(byte[] avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        updateAvatar = true;
    }

    @Override
    public void setAvatar(InputStream avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        updateAvatar = true;
    }

    @Override
    public void setAvatar(InputStream avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        updateAvatar = true;
    }

    @Override
    public void removeAvatar() {
        this.avatar = null;
        updateAvatar = true;
    }

    private RestRequest<Webhook> setUrlParameters(RestRequest<Webhook> request) {
        //see: https://discord.com/developers/docs/resources/webhook#execute-webhook-querystring-params
        if (channel == null) { //changing channel doesn't work when using token.
            Optional<String> token = webhook.asIncomingWebhook().map(IncomingWebhook::getToken);
            if (token.isPresent()) {
                return request.setUrlParameters(webhook.getIdAsString(), token.get());
            }
        }
        return request.setUrlParameters(webhook.getIdAsString());
    }

    @Override
    public CompletableFuture<Webhook> update() {
        boolean patchWebhook = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchWebhook = true;
        }
        if (channel != null) {
            body.put("channel_id", channel.getIdAsString());
            patchWebhook = true;
        }
        if (updateAvatar) {
            if (avatar == null) {
                body.putNull("avatar");
            }
            patchWebhook = true;
        }
        if (patchWebhook) {
            if (avatar != null) {
                return avatar.asByteArray(webhook.getApi()).thenAccept(bytes -> {
                    String base64Avatar = "data:image/" + avatar.getFileType() + ";base64,"
                            + Base64.getEncoder().encodeToString(bytes);
                    body.put("avatar", base64Avatar);
                }).thenCompose(aVoid ->
                        setUrlParameters(new RestRequest<>(webhook.getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK))
                        .setBody(body)
                        .setAuditLogReason(reason)
                        .execute(result -> WebhookImpl.createWebhook(webhook.getApi(), result.getJsonBody())));
            }
            return setUrlParameters(new RestRequest<>(webhook.getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK))
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> WebhookImpl.createWebhook(webhook.getApi(), result.getJsonBody()));
        } else {
            return CompletableFuture.completedFuture(webhook);
        }
    }

}
