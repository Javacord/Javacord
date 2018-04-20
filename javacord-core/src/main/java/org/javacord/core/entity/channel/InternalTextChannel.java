package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.TextChannelAttachableListener;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.ChannelPinsUpdateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.listener.user.UserStartTypingListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.MessageSetImpl;
import org.javacord.core.entity.webhook.WebhookImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface InternalTextChannel extends TextChannel {

    @Override
    default CompletableFuture<Void> type() {
        return new RestRequest<Void>(getApi(), RestMethod.POST, RestEndpoint.CHANNEL_TYPING)
                .setRatelimitRetries(0)
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
                .setRatelimitRetries(0)
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
                    for (JsonNode webhook : result.getJsonBody()) {
                        webhooks.add(new WebhookImpl(getApi(), webhook));
                    }
                    return Collections.unmodifiableList(webhooks);
                });
    }

    @Override
    default ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                TextChannel.class, getId(), MessageCreateListener.class, listener);
    }

    @Override
    default List<MessageCreateListener> getMessageCreateListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(TextChannel.class, getId(), MessageCreateListener.class);
    }

    @Override
    default ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                TextChannel.class, getId(), UserStartTypingListener.class, listener);
    }

    @Override
    default List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                TextChannel.class, getId(), UserStartTypingListener.class);
    }

    @Override
    default ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                TextChannel.class, getId(), MessageDeleteListener.class, listener);
    }

    @Override
    default List<MessageDeleteListener> getMessageDeleteListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(TextChannel.class, getId(), MessageDeleteListener.class);
    }

    @Override
    default ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(TextChannel.class, getId(), MessageEditListener.class, listener);
    }

    @Override
    default List<MessageEditListener> getMessageEditListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(TextChannel.class, getId(), MessageEditListener.class);
    }

    @Override
    default ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(TextChannel.class, getId(), ReactionAddListener.class, listener);
    }

    @Override
    default List<ReactionAddListener> getReactionAddListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(TextChannel.class, getId(), ReactionAddListener.class);
    }

    @Override
    default ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                TextChannel.class, getId(), ReactionRemoveListener.class, listener);
    }

    @Override
    default List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(TextChannel.class, getId(), ReactionRemoveListener.class);
    }

    @Override
    default ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            ReactionRemoveAllListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(TextChannel.class, getId(), ReactionRemoveAllListener.class, listener);
    }

    @Override
    default List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(TextChannel.class, getId(), ReactionRemoveAllListener.class);
    }

    @Override
    default ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(
            ChannelPinsUpdateListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(TextChannel.class, getId(), ChannelPinsUpdateListener.class, listener);
    }

    @Override
    default List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(TextChannel.class, getId(), ChannelPinsUpdateListener.class);
    }

    @Override
    default ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(TextChannel.class, getId(), CachedMessagePinListener.class, listener);
    }

    @Override
    default List<CachedMessagePinListener> getCachedMessagePinListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(TextChannel.class, getId(), CachedMessagePinListener.class);
    }

    @Override
    default ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(
            CachedMessageUnpinListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(TextChannel.class, getId(), CachedMessageUnpinListener.class, listener);
    }

    @Override
    default List<CachedMessageUnpinListener> getCachedMessageUnpinListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(TextChannel.class, getId(), CachedMessageUnpinListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends TextChannelAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends TextChannelAttachableListener>>
                    addTextChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(TextChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(TextChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends TextChannelAttachableListener & ObjectAttachableListener> void
            removeTextChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(TextChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((DiscordApiImpl) getApi()).removeObjectListener(TextChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends TextChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getTextChannelAttachableListeners() {
        Map<T, List<Class<T>>> textChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(TextChannel.class, getId());
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> textChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        return textChannelListeners;
    }

    @Override
    default <T extends TextChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(TextChannel.class, getId(), listenerClass, listener);
    }

}
