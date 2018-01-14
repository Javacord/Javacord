package de.btobastian.javacord.entities;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update webhooks.
 */
public class WebhookUpdater {

    /**
     * The webhook to update.
     */
    protected final Webhook webhook;

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
    protected BufferedImage avatar = null;

    /**
     * Whether the avatar should be updated or not.
     */
    protected boolean updateAvatar = false;

    /**
     * Creates a new webhook updater.
     *
     * @param webhook The webhook to update.
     */
    public WebhookUpdater(Webhook webhook) {
        this.webhook = webhook;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Queues the channel to be updated.
     *
     * @param channel The new channel of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setChannel(ServerTextChannel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The new avatar of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater removeAvatar() {
        return setAvatar(null);
    }

    /**
     * Performs the queued updates.
     *
     * @return The updated webhook or the current instance if no updates were queued.
     */
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
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(avatar, "jpg", os);
                String base64Avatar = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
                body.put("avatar", base64Avatar);
            } catch (IOException e) {
                CompletableFuture<Webhook> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
            patchWebhook = true;
        }
        if (patchWebhook) {
            return new RestRequest<Webhook>(webhook.getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK)
                    .setUrlParameters(webhook.getIdAsString())
                    .setBody(body)
                    .execute(result -> new ImplWebhook(webhook.getApi(), result.getJsonBody()));
        } else {
            return CompletableFuture.completedFuture(webhook);
        }
    }

}
