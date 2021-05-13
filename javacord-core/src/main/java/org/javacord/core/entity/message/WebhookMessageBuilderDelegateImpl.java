package org.javacord.core.entity.message;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.internal.WebhookMessageBuilderDelegate;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link WebhookMessageBuilderDelegate}.
 */
public class WebhookMessageBuilderDelegateImpl extends WebhookMessageBuilderBaseDelegateImpl
        implements WebhookMessageBuilderDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(WebhookMessageBuilderDelegateImpl.class);

    /**
     * The avatar the webhook should use.
     */
    private URL avatarUrl = null;

    /**
     * The display name the webhook should use.
     */
    private String displayName = null;

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setDisplayAvatar(URL avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public void setDisplayAvatar(Icon avatar) {
        this.avatarUrl = avatar.getUrl();
    }

    @Override
    public CompletableFuture<Message> send(IncomingWebhook webhook) {
        return send(webhook.getIdAsString(), webhook.getToken(), displayName, avatarUrl, true, webhook.getApi());
    }

    @Override
    public CompletableFuture<Message> send(DiscordApi api, String webhookId, String webhookToken) {
        return send(webhookId, webhookToken, displayName, avatarUrl, true, api);
    }

    @Override
    public CompletableFuture<Void> sendSilently(IncomingWebhook webhook) {
        return sendSilently(webhook.getApi(), webhook.getIdAsString(), webhook.getToken());
    }

    @Override
    public CompletableFuture<Void> sendSilently(DiscordApi api, String webhookId, String webhookToken) {
        return send(webhookId, webhookToken, displayName, avatarUrl, false, api).thenApply(m -> null);
    }
}
