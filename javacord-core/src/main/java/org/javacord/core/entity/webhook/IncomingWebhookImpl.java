package org.javacord.core.entity.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.WebhookType;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link IncomingWebhook}.
 */
public class IncomingWebhookImpl extends WebhookImpl implements IncomingWebhook {

    private final String token;

    /**
     * Creates a new incoming webhook.
     *
     * @param api  The discord api instance.
     * @param data The json data of the webhook.
     */
    public IncomingWebhookImpl(DiscordApi api, JsonNode data) {
        super(api, data);
        token = data.get("token").asText();
    }

    @Override
    public Optional<IncomingWebhook> asIncomingWebhook() {
        return Optional.of(this);
    }

    @Override
    public WebhookType getType() {
        return WebhookType.INCOMING;
    }

    @Override
    public boolean isIncomingWebhook() {
        return true;
    }

    @Override
    public boolean isChannelFollowerWebhook() {
        return false;
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.WEBHOOK)
                .setUrlParameters(getIdAsString(), getToken())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    /**
     * Gets the secure token of the webhook.
     *
     * @return The secure token of the webhook.
     */
    public String getToken() {
        return token;
    }
}
