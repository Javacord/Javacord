package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.core.interaction.InteractionImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public class InteractionMessageBuilderDelegateImpl extends MessageBuilderBaseDelegateImpl
        implements InteractionMessageBuilderDelegate {

    @Override
    public CompletableFuture<Void> sendInitialResponse(InteractionBase interaction) {
        ObjectNode topBody = JsonNodeFactory.instance.objectNode();
        topBody.put("type", InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE.getId());
        ObjectNode body = topBody.putObject("data");
        prepareInteractionWebhookBodyParts(body);

        return new RestRequest<Void>(interaction.getApi(), RestMethod.POST, RestEndpoint.INTERACTION_RESPONSE)
                .setUrlParameters(interaction.getIdAsString(), interaction.getToken())
                .consumeGlobalRatelimit(false)
                .includeAuthorizationHeader(false)
                .setBody(topBody)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> deleteInitialResponse(InteractionBase interaction) {
        return new RestRequest<Void>(interaction.getApi(), RestMethod.DELETE,
                RestEndpoint.ORIGINAL_INTERACTION_RESPONSE)
                .setUrlParameters(Long.toUnsignedString(interaction.getApplicationId()), interaction.getToken())
                .consumeGlobalRatelimit(false)
                .includeAuthorizationHeader(false)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Message> editOriginalResponse(InteractionBase interaction) {
        RestRequest<Message> request = new RestRequest<Message>(interaction.getApi(), RestMethod.PATCH,
                RestEndpoint.ORIGINAL_INTERACTION_RESPONSE)
                .setUrlParameters(Long.toUnsignedString(interaction.getApplicationId()), interaction.getToken())
                .consumeGlobalRatelimit(false)
                .includeAuthorizationHeader(false);

        return executeResponse(request);
    }

    @Override
    public CompletableFuture<Message> sendFollowupMessage(InteractionBase interaction) {
        RestRequest<Message> request =
                new RestRequest<Message>(interaction.getApi(), RestMethod.POST, RestEndpoint.WEBHOOK_SEND)
                        .setUrlParameters(Long.toUnsignedString(interaction.getApplicationId()), interaction.getToken())
                        .consumeGlobalRatelimit(false)
                        .includeAuthorizationHeader(false);

        return executeResponse(request);
    }

    @Override
    public CompletableFuture<Void> updateOriginalMessage(InteractionBase interaction) {
        ObjectNode topBody = JsonNodeFactory.instance.objectNode();
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        prepareCommonWebhookMessageBodyParts(data);
        prepareComponents(data, true);
        topBody.put("type", InteractionCallbackType.UPDATE_MESSAGE.getId());
        topBody.set("data", data);

        return new RestRequest<Void>(interaction.getApi(),
                RestMethod.POST, RestEndpoint.INTERACTION_RESPONSE)
                .setUrlParameters(interaction.getIdAsString(), interaction.getToken())
                .consumeGlobalRatelimit(false)
                .includeAuthorizationHeader(false)
                .setBody(topBody)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> deleteFollowupMessage(InteractionBase interaction, String messageId) {
        return new RestRequest<Void>(interaction.getApi(), RestMethod.DELETE,
                RestEndpoint.WEBHOOK_MESSAGE)
                .setUrlParameters(Long.toUnsignedString(interaction.getApplicationId()),
                        interaction.getToken(), messageId)
                .consumeGlobalRatelimit(false)
                .includeAuthorizationHeader(false)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Message> editFollowupMessage(InteractionBase interaction, String messageId) {
        RestRequest<Message> request = new RestRequest<Message>(interaction.getApi(), RestMethod.PATCH,
                RestEndpoint.WEBHOOK_MESSAGE)
                .setUrlParameters(Long.toUnsignedString(interaction.getApplicationId()),
                        interaction.getToken(), messageId)
                .consumeGlobalRatelimit(false)
                .includeAuthorizationHeader(false);

        return executeResponse(request);
    }

    @Override
    public void copy(InteractionBase interaction) {
        ((InteractionImpl) interaction).asMessageComponentInteraction()
                .map(MessageComponentInteraction::getMessage)
                .ifPresent(this::copy);
    }

    private CompletableFuture<Message> executeResponse(RestRequest<Message> request) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareInteractionWebhookBodyParts(body);

        return checkForAttachmentsAndExecuteRequest(
                jsonNode -> request.getApi().getTextChannelById(jsonNode.get("channel_id").asLong())
                        .orElseThrow(() -> new NoSuchElementException("TextChannel is not cached")), body, request);
    }

    private void prepareInteractionWebhookBodyParts(ObjectNode body) {
        prepareCommonWebhookMessageBodyParts(body);
        prepareComponents(body, true);
    }

}