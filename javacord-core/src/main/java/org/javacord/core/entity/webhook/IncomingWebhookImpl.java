package org.javacord.core.entity.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.WebhookType;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    /**
     * Gets all incoming webhooks with a token from a json array.
     *
     * @param api The discord api instance.
     * @param jsonArray The json array of the webhooks.
     * @return All the incoming webhooks from the array.
     */
    public static List<IncomingWebhook> createIncomingWebhooksFromJsonArray(DiscordApi api, JsonNode jsonArray) {
        List<IncomingWebhook> webhooks = new ArrayList<>();
        for (JsonNode webhookJson : jsonArray) {
            if (WebhookType.fromValue(webhookJson.get("type").asInt()) == WebhookType.INCOMING
                    && webhookJson.hasNonNull("token")) { // check if it has a token to create IncomingWebhook
                webhooks.add(new IncomingWebhookImpl(api, webhookJson));
            }
        }
        return Collections.unmodifiableList(webhooks);
    }

    @Override
    public Optional<IncomingWebhook> asIncomingWebhook() {
        // important, as this is only possible with incoming webhooks with token
        return Optional.of(this);
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
