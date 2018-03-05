package org.javacord.entity.message.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.DiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.UncachedMessageUtil;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.message.embed.impl.ImplEmbedFactory;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
public class ImplUncachedMessageUtil implements UncachedMessageUtil {

    private final DiscordApi api;

    public ImplUncachedMessageUtil(DiscordApi api) {
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
                                    .setRatelimitRetries(0)
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
                ((ImplEmbedFactory) embed.getFactory()).toJsonNode(body.putObject("embed"));
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
    
}
