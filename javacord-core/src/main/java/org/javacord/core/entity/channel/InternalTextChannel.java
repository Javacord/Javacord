package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.MessageSetImpl;
import org.javacord.core.entity.webhook.IncomingWebhookImpl;
import org.javacord.core.entity.webhook.WebhookImpl;
import org.javacord.core.listener.channel.InternalTextChannelAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface InternalTextChannel extends TextChannel, InternalTextChannelAttachableListenerManager {

    @Override
    default CompletableFuture<Void> type() {
        return new RestRequest<Void>(getApi(), RestMethod.POST, RestEndpoint.CHANNEL_TYPING)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    @Override
    default CompletableFuture<Void> bulkDelete(long... messageIds) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        ArrayNode messages = body.putArray("messages");
        LongStream.of(messageIds).boxed()
                .map(Long::toUnsignedString)
                .forEach(messages::add);

        return new RestRequest<Void>(getApi(), RestMethod.POST, RestEndpoint.MESSAGES_BULK_DELETE)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .execute(result -> null);
    }

    @Override
    default CompletableFuture<Message> getMessageById(long id) {
        return getApi().getCachedMessageById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new RestRequest<Message>(getApi(), RestMethod.GET, RestEndpoint.MESSAGE)
                .setUrlParameters(getIdAsString(), Long.toUnsignedString(id))
                .execute(result -> ((DiscordApiImpl) getApi()).getOrCreateMessage(this, result.getJsonBody())));
    }

    @Override
    default CompletableFuture<MessageSet> getPins() {
        return new RestRequest<MessageSet>(getApi(), RestMethod.GET, RestEndpoint.PINS)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<Message> pins = StreamSupport.stream(result.getJsonBody().spliterator(), false)
                            .map(pinJson -> ((DiscordApiImpl) getApi()).getOrCreateMessage(this, pinJson))
                            .collect(Collectors.toList());
                    return new MessageSetImpl(pins);
                });
    }

    @Override
    default CompletableFuture<MessageSet> getMessages(int limit) {
        return MessageSetImpl.getMessages(this, limit);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesUntil(Predicate<Message> condition) {
        return MessageSetImpl.getMessagesUntil(this, condition);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesWhile(Predicate<Message> condition) {
        return MessageSetImpl.getMessagesWhile(this, condition);
    }

    @Override
    default Stream<Message> getMessagesAsStream() {
        return MessageSetImpl.getMessagesAsStream(this);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesBefore(int limit, long before) {
        return MessageSetImpl.getMessagesBefore(this, limit, before);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesBeforeUntil(Predicate<Message> condition, long before) {
        return MessageSetImpl.getMessagesBeforeUntil(this, condition, before);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesBeforeWhile(Predicate<Message> condition, long before) {
        return MessageSetImpl.getMessagesBeforeWhile(this, condition, before);
    }

    @Override
    default Stream<Message> getMessagesBeforeAsStream(long before) {
        return MessageSetImpl.getMessagesBeforeAsStream(this, before);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesAfter(int limit, long after) {
        return MessageSetImpl.getMessagesAfter(this, limit, after);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesAfterUntil(Predicate<Message> condition, long after) {
        return MessageSetImpl.getMessagesAfterUntil(this, condition, after);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesAfterWhile(Predicate<Message> condition, long after) {
        return MessageSetImpl.getMessagesAfterWhile(this, condition, after);
    }

    @Override
    default Stream<Message> getMessagesAfterAsStream(long after) {
        return MessageSetImpl.getMessagesAfterAsStream(this, after);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesAround(int limit, long around) {
        return MessageSetImpl.getMessagesAround(this, limit, around);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesAroundUntil(Predicate<Message> condition, long around) {
        return MessageSetImpl.getMessagesAroundUntil(this, condition, around);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesAroundWhile(Predicate<Message> condition, long around) {
        return MessageSetImpl.getMessagesAroundWhile(this, condition, around);
    }

    @Override
    default Stream<Message> getMessagesAroundAsStream(long around) {
        return MessageSetImpl.getMessagesAroundAsStream(this, around);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesBetween(long from, long to) {
        return MessageSetImpl.getMessagesBetween(this, from, to);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesBetweenUntil(Predicate<Message> condition, long from, long to) {
        return MessageSetImpl.getMessagesBetweenUntil(this, condition, from, to);
    }

    @Override
    default CompletableFuture<MessageSet> getMessagesBetweenWhile(Predicate<Message> condition, long from, long to) {
        return MessageSetImpl.getMessagesBetweenWhile(this, condition, from, to);
    }

    @Override
    default Stream<Message> getMessagesBetweenAsStream(long from, long to) {
        return MessageSetImpl.getMessagesBetweenAsStream(this, from, to);
    }

    @Override
    default CompletableFuture<List<Webhook>> getWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), RestMethod.GET, RestEndpoint.CHANNEL_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<Webhook> webhooks = new ArrayList<>();
                    for (JsonNode webhookJson : result.getJsonBody()) {
                        webhooks.add(WebhookImpl.createWebhook(getApi(), webhookJson));
                    }
                    return Collections.unmodifiableList(webhooks);
                });
    }

    @Override
    default CompletableFuture<List<IncomingWebhook>> getIncomingWebhooks() {
        return new RestRequest<List<IncomingWebhook>>(getApi(), RestMethod.GET, RestEndpoint.CHANNEL_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<IncomingWebhook> webhooks = new ArrayList<>();
                    for (JsonNode webhookJson : result.getJsonBody()) {
                        if (webhookJson.get("type").asText().equals("1")) {
                            webhooks.add(new IncomingWebhookImpl(getApi(), webhookJson));
                        }
                    }
                    return Collections.unmodifiableList(webhooks);
                });
    }
}
