package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.UncachedMessageUtil;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.embed.EmbedBuilderDelegateImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class provides methods to interact with messages without having an instance of it.
 */
public class UncachedMessageUtilImpl implements UncachedMessageUtil {

    private final DiscordApiImpl api;

    /**
     * Creates a new instance of this class.
     *
     * @param api The discord api instance.
     */
    public UncachedMessageUtilImpl(DiscordApiImpl api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<Void> delete(long channelId, long messageId) {
        return delete(channelId, messageId, null);
    }

    @Override
    public CompletableFuture<Void> delete(String channelId, String messageId) {
        return delete(channelId, messageId, null);
    }

    @Override
    public CompletableFuture<Void> delete(long channelId, long messageId, String reason) {
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.MESSAGE_DELETE)
                .setUrlParameters(Long.toUnsignedString(channelId), Long.toUnsignedString(messageId))
                .setRatelimitRetries(250)
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> delete(String channelId, String messageId, String reason) {
        try {
            return delete(Long.parseLong(channelId), Long.parseLong(messageId), reason);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> deleteAll(long channelId, long... messageIds) {
        // split by younger than two weeks / older than two weeks
        Instant twoWeeksAgo = Instant.now().minus(14, ChronoUnit.DAYS);
        Map<Boolean, List<Long>> messageIdsByAge = Arrays.stream(messageIds).distinct().boxed()
                .collect(Collectors.groupingBy(
                        messageId -> DiscordEntity.getCreationTimestamp(messageId).isAfter(twoWeeksAgo)));

        AtomicInteger batchCounter = new AtomicInteger();
        return CompletableFuture.allOf(Stream.concat(
                // for messages younger than 2 weeks
                messageIdsByAge.getOrDefault(true, Collections.emptyList()).stream()
                        // send batches of 100 messages
                        .collect(Collectors.groupingBy(messageId -> batchCounter.getAndIncrement() / 100))
                        .values().stream()
                        .map(messageIdBatch -> {
                            // do not use batch deletion for a single message
                            if (messageIdBatch.size() == 1) {
                                return Message.delete(api, channelId, messageIdBatch.get(0));
                            }

                            ObjectNode body = JsonNodeFactory.instance.objectNode();
                            ArrayNode messages = body.putArray("messages");
                            messageIdBatch.stream()
                                    .map(Long::toUnsignedString)
                                    .forEach(messages::add);

                            return new RestRequest<Void>(api, RestMethod.POST, RestEndpoint.MESSAGES_BULK_DELETE)
                                    .setUrlParameters(Long.toUnsignedString(channelId))
                                    .setBody(body)
                                    .execute(result -> null);
                        }),
                // for messages older than 2 weeks use single message deletion
                messageIdsByAge.getOrDefault(false, Collections.emptyList()).stream()
                        .map(messageId -> Message.delete(api, channelId, messageId))
        ).toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<Void> deleteAll(String channelId, String... messageIds) {
        long[] messageLongIds = Arrays.stream(messageIds).filter(s -> {
            try {
                //noinspection ResultOfMethodCallIgnored
                Long.parseLong(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }).mapToLong(Long::parseLong).toArray();
        return deleteAll(Long.parseLong(channelId), messageLongIds);
    }

    @Override
    public CompletableFuture<Void> deleteAll(Message... messages) {
        return CompletableFuture.allOf(
                Arrays.stream(messages)
                        .collect(Collectors.groupingBy(message -> message.getChannel().getId(),
                                Collectors.mapping(Message::getId, Collectors.toList())))
                        .entrySet().stream()
                        .map(entry -> deleteAll(entry.getKey(),
                                entry.getValue().stream().mapToLong(Long::longValue).toArray()))
                        .toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<Void> deleteAll(Iterable<Message> messages) {
        return deleteAll(StreamSupport.stream(messages.spliterator(), false).toArray(Message[]::new));
    }

    @Override
    public CompletableFuture<Void> edit(long channelId, long messageId, String content) {
        return edit(channelId, messageId, content, true, null, false);
    }

    @Override
    public CompletableFuture<Void> edit(String channelId, String messageId, String content) {
        return edit(channelId, messageId, content, true, null, false);
    }

    @Override
    public CompletableFuture<Void> edit(long channelId, long messageId, EmbedBuilder embed) {
        return edit(channelId, messageId, null, false, embed, true);
    }

    @Override
    public CompletableFuture<Void> edit(String channelId, String messageId, EmbedBuilder embed) {
        return edit(channelId, messageId, null, false, embed, true);
    }

    @Override
    public CompletableFuture<Void> edit(
            long channelId, long messageId, String content, EmbedBuilder embed) {
        return edit(channelId, messageId, content, true, embed, true);
    }

    @Override
    public CompletableFuture<Void> edit(
            String channelId, String messageId, String content, EmbedBuilder embed) {
        return edit(channelId, messageId, content, true, embed, true);
    }

    @Override
    public CompletableFuture<Void> edit(long channelId, long messageId, String content,
                                        boolean updateContent, EmbedBuilder embed, boolean updateEmbed) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (updateContent) {
            if (content == null) {
                body.putNull("content");
            } else {
                body.put("content", content);
            }
        }
        if (updateEmbed) {
            if (embed == null) {
                body.putNull("embed");
            } else {
                ((EmbedBuilderDelegateImpl) embed.getDelegate()).toJsonNode(body.putObject("embed"));
            }
        }
        return new RestRequest<Void>(api, RestMethod.PATCH, RestEndpoint.MESSAGE)
                .setUrlParameters(Long.toUnsignedString(channelId), Long.toUnsignedString(messageId))
                .setBody(body)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> edit(String channelId, String messageId, String content,
                                        boolean updateContent, EmbedBuilder embed, boolean updateEmbed) {
        try {
            return edit(Long.parseLong(channelId), Long.parseLong(messageId), content, true, embed, true);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> removeContent(long channelId, long messageId) {
        return edit(channelId, messageId, null, true, null, false);
    }

    @Override
    public CompletableFuture<Void> removeContent(String channelId, String messageId) {
        return edit(channelId, messageId, null, true, null, false);
    }

    @Override
    public CompletableFuture<Void> removeEmbed(long channelId, long messageId) {
        return edit(channelId, messageId, null, false, null, true);
    }

    @Override
    public CompletableFuture<Void> removeEmbed(String channelId, String messageId) {
        return edit(channelId, messageId, null, false, null, true);
    }

    @Override
    public CompletableFuture<Void> removeContentAndEmbed(long channelId, long messageId) {
        return edit(channelId, messageId, null, true, null, true);
    }

    @Override
    public CompletableFuture<Void> removeContentAndEmbed(String channelId, String messageId) {
        return edit(channelId, messageId, null, true, null, true);
    }

    @Override
    public CompletableFuture<Void> addReaction(long channelId, long messageId, String unicodeEmoji) {
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.REACTION)
                .setUrlParameters(
                        Long.toUnsignedString(channelId), Long.toUnsignedString(messageId), unicodeEmoji, "@me")
                .setRatelimitRetries(500)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> addReaction(
            String channelId, String messageId, String unicodeEmoji) {
        try {
            return addReaction(Long.parseLong(channelId), Long.parseLong(messageId), unicodeEmoji);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> addReaction(long channelId, long messageId, Emoji emoji) {
        String value = emoji.asUnicodeEmoji().orElseGet(() ->
                emoji.asCustomEmoji()
                        .map(e -> e.getName() + ":" + e.getIdAsString())
                        .orElse("UNKNOWN")
        );
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.REACTION)
                .setUrlParameters(Long.toUnsignedString(channelId), Long.toUnsignedString(messageId), value, "@me")
                .setRatelimitRetries(500)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> addReaction(String channelId, String messageId, Emoji emoji) {
        try {
            return addReaction(Long.parseLong(channelId), Long.parseLong(messageId), emoji);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> removeAllReactions(long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(Long.toUnsignedString(channelId), Long.toUnsignedString(messageId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> removeAllReactions(String channelId, String messageId) {
        try {
            return removeAllReactions(Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> pin(long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.PINS)
                .setUrlParameters(Long.toUnsignedString(channelId), Long.toUnsignedString(messageId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> pin(String channelId, String messageId) {
        try {
            return pin(Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> unpin(long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.PINS)
                .setUrlParameters(Long.toUnsignedString(channelId), Long.toUnsignedString(messageId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> unpin(String channelId, String messageId) {
        try {
            return unpin(Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<List<User>> getUsersWhoReactedWithEmoji(long channelId, long messageId, Emoji emoji) {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        api.getThreadPool().getExecutorService().submit(() -> {
            try {
                final String value = emoji.asUnicodeEmoji().orElseGet(() -> emoji.asCustomEmoji()
                        .map(e -> e.getName() + ":" + e.getIdAsString()).orElse("UNKNOWN"));
                List<User> users = new ArrayList<>();
                boolean requestMore = true;
                while (requestMore) {
                    RestRequest<List<User>> request =
                            new RestRequest<List<User>>(api, RestMethod.GET, RestEndpoint.REACTION)
                                    .setUrlParameters(
                                            Long.toUnsignedString(channelId), Long.toUnsignedString(messageId), value)
                                    .addQueryParameter("limit", "100")
                                    .setRatelimitRetries(250);
                    if (!users.isEmpty()) {
                        request.addQueryParameter("after", users.get(users.size() - 1).getIdAsString());
                    }
                    List<User> incompleteUsers = request.execute(result -> {
                        List<User> paginatedUsers = new ArrayList<>();
                        for (JsonNode userJson : result.getJsonBody()) {
                            paginatedUsers.add(api.getOrCreateUser(userJson));
                        }
                        return Collections.unmodifiableList(paginatedUsers);
                    }).join();
                    users.addAll(incompleteUsers);
                    requestMore = incompleteUsers.size() >= 100;
                }
                future.complete(Collections.unmodifiableList(users));
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<List<User>> getUsersWhoReactedWithEmoji(String channelId, String messageId, Emoji emoji) {
        try {
            return getUsersWhoReactedWithEmoji(Long.parseLong(channelId), Long.parseLong(messageId), emoji);
        } catch (NumberFormatException e) {
            CompletableFuture<List<User>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> removeUserReactionByEmoji(long channelId, long messageId, Emoji emoji, User user) {
        String value = emoji.asUnicodeEmoji().orElseGet(() ->
                emoji.asCustomEmoji().map(e -> e.getName() + ":" + e.getIdAsString()).orElse("UNKNOWN"));
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(
                        Long.toUnsignedString(channelId),
                        Long.toUnsignedString(messageId),
                        value,
                        user.isYourself() ? "@me" : user.getIdAsString())
                .setRatelimitRetries(250)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> removeUserReactionByEmoji(String channelId, String messageId, Emoji emoji,
                                                             User user) {
        try {
            return removeUserReactionByEmoji(Long.parseLong(channelId), Long.parseLong(messageId), emoji, user);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public ListenerManager<MessageDeleteListener> addMessageDeleteListener(
            long messageId, MessageDeleteListener listener) {
        return api.addObjectListener(
                Message.class, messageId, MessageDeleteListener.class, listener);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners(long messageId) {
        return api.getObjectListeners(Message.class, messageId, MessageDeleteListener.class);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners(String messageId) {
        try {
            return getMessageDeleteListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public ListenerManager<MessageEditListener> addMessageEditListener(long messageId, MessageEditListener listener) {
        return api.addObjectListener(Message.class, messageId, MessageEditListener.class, listener);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners(long messageId) {
        return api.getObjectListeners(Message.class, messageId, MessageEditListener.class);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners(String messageId) {
        try {
            return getMessageEditListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(long messageId, ReactionAddListener listener) {
        return api.addObjectListener(Message.class, messageId, ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners(long messageId) {
        return api.getObjectListeners(Message.class, messageId, ReactionAddListener.class);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners(String messageId) {
        try {
            return getReactionAddListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(long messageId,
                                                                             ReactionRemoveListener listener) {
        return api.addObjectListener(Message.class, messageId, ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners(long messageId) {
        return api.getObjectListeners(Message.class, messageId, ReactionRemoveListener.class);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners(String messageId) {
        try {
            return getReactionRemoveListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(long messageId,
                                                                                   ReactionRemoveAllListener listener) {
        return api.addObjectListener(Message.class, messageId, ReactionRemoveAllListener.class, listener);
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners(long messageId) {
        return api.getObjectListeners(Message.class, messageId, ReactionRemoveAllListener.class);
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners(String messageId) {
        try {
            return getReactionRemoveAllListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addMessageAttachableListener(long messageId, T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(MessageAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> api.addObjectListener(
                        Message.class, messageId, listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    public <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addMessageAttachableListener(String messageId, T listener) {
        try {
            return addMessageAttachableListener(Long.valueOf(messageId), listener);
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public <T extends MessageAttachableListener & ObjectAttachableListener> void removeListener(
            long messageId, Class<T> listenerClass, T listener) {
        api.removeObjectListener(Message.class, messageId, listenerClass, listener);
    }

    @Override
    public <T extends MessageAttachableListener & ObjectAttachableListener> void removeListener(
            String messageId, Class<T> listenerClass, T listener) {
        try {
            removeListener(Long.valueOf(messageId), listenerClass, listener);
        } catch (NumberFormatException ignored) { }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            long messageId, T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(MessageAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> removeListener(messageId, listenerClass, listener));
    }

    @Override
    public <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            String messageId, T listener) {
        try {
            removeMessageAttachableListener(Long.valueOf(messageId), listener);
        } catch (NumberFormatException ignored) { }
    }

    @Override
    public <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getMessageAttachableListeners(long messageId) {
        return api.getObjectListeners(Message.class, messageId);
    }

    @Override
    public <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getMessageAttachableListeners(String messageId) {
        try {
            return getMessageAttachableListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) {
            return Collections.emptyMap();
        }
    }

}
