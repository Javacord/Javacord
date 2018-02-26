package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.Webhook;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageSet;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.message.impl.ImplMessageSet;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.listeners.ChannelAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.message.CachedMessagePinListener;
import de.btobastian.javacord.listeners.message.CachedMessageUnpinListener;
import de.btobastian.javacord.listeners.message.ChannelPinsUpdateListener;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.NonThrowingAutoCloseable;
import de.btobastian.javacord.utils.cache.MessageCache;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class represents a text channel.
 */
public interface TextChannel extends Channel, Messageable {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(TextChannel.class);

    /**
     * Displays the "xyz is typing..." message.
     * The message automatically disappears after 10 seconds or after sending a message.
     * No ratelimit retries are done at all.
     * If the ratelimit is reached already, then the returned future will complete exceptionally right on the first try
     * with a {@code RatelimitException}.
     *
     * @return A future to tell us if the action was successful.
     * @see #typeContinuously()
     * @see #typeContinuously(Consumer)
     */
    default CompletableFuture<Void> type() {
        return new RestRequest<Void>(getApi(), RestMethod.POST, RestEndpoint.CHANNEL_TYPING)
                .setRatelimitRetries(0)
                .setUrlParameters(String.valueOf(getId()))
                .execute(result -> null);
    }

    /**
     * Displays the "xyz is typing..." message continuously, starting immediately.
     * The message is continuously displayed if not quit using the returned {@code AutoCloseable}.
     * Sending a message will make the message go away shortly, but it will return immediately if not cancelled using
     * the {@code AutoCloseable}. This can be used in a try-with-resources block like
     * <code>try (NonThrowingAutoCloseable typingIndicator = textChannel.typeContinuously())
     * { /* do lengthy stuff &#42;/ } sendReply();</code>.
     * <p>
     * The typing indicator will immediately be shown. To delay the display of the first typing indicator, use
     * {@link #typeContinuouslyAfter(long, TimeUnit)}. This can be useful if the task you do can be finished in very
     * short time which could cause the typing indicator and the response message being sent at the same time and the
     * typing indicator could be shown for 10 seconds even if the message was sent already.
     * <p>
     * Any occurring exceptions including ratelimit exceptions are suppressed. If you want to handle exceptions, use
     * {@link #typeContinuously(Consumer)} or {@link #typeContinuouslyAfter(long, TimeUnit, Consumer)}.
     *
     * @return An auto-closable to stop sending the typing indicator.
     * @see #type()
     * @see #typeContinuously(Consumer)
     * @see #typeContinuouslyAfter(long, TimeUnit)
     * @see #typeContinuouslyAfter(long, TimeUnit, Consumer)
     */
    default NonThrowingAutoCloseable typeContinuously() {
        return typeContinuouslyAfter(0, TimeUnit.NANOSECONDS, null);
    }

    /**
     * Displays the "xyz is typing..." message continuously, starting delayed.
     * The message is continuously displayed if not quit using the returned {@code AutoCloseable}.
     * Sending a message will make the message go away shortly, but it will return immediately if not cancelled using
     * the {@code AutoCloseable}. This can be used in a try-with-resources block like
     * <code>try (NonThrowingAutoCloseable typingIndicator =
     * textChannel.typeContinuouslyAfter(500, TimeUnit.MILLISECONDS)) { /* do lengthy stuff &#42;/ }
     * sendReply();</code>.
     * <p>
     * The typing indicator will be shown delayed. This can be useful if the task you do can be finished in very short
     * time which could cause the typing indicator and the response message being sent at the same time and the typing
     * indicator could be shown for 10 seconds even if the message was sent already. With the delay this is
     * compensated, because if the returned {@code AutoCloseable} is closed before the delay is over, no typing
     * indicator will be sent at all.
     * <p>
     * Any occurring exceptions including ratelimit exceptions are suppressed. If you want to handle exceptions, use
     * {@link #typeContinuously(Consumer)} or {@link #typeContinuouslyAfter(long, TimeUnit, Consumer)}.
     *
     * @param delay The delay to wait until the first typing indicator is sent.
     * @param timeUnit The time unit of the delay value.
     * @return An auto-closable to stop sending the typing indicator.
     * @see #type()
     * @see #typeContinuously()
     * @see #typeContinuously(Consumer)
     * @see #typeContinuouslyAfter(long, TimeUnit, Consumer)
     */
    default NonThrowingAutoCloseable typeContinuouslyAfter(long delay, TimeUnit timeUnit) {
        return typeContinuouslyAfter(delay, timeUnit, null);
    }

    /**
     * Displays the "xyz is typing..." message continuously, starting immediately.
     * The message is continuously displayed if not quit using the returned {@code AutoCloseable}.
     * Sending a message will make the message go away shortly, but it will return immediately if not cancelled using
     * the {@code AutoCloseable}. This can be used in a try-with-resources block like
     * <code>try (NonThrowingAutoCloseable typingIndicator =
     * textChannel.typeContinuously(((Function&lt;Throwable, ?&gt;) Javacord::exceptionLogger)::apply))
     * { /* do lengthy stuff &#42;/ } sendReply();</code>.
     * <p>
     * The typing indicator will immediately be shown. To delay the display of the first typing indicator, use
     * {@link #typeContinuouslyAfter(long, TimeUnit)}. This can be useful if the task you do can be finished in very
     * short time which could cause the typing indicator and the response message being sent at the same time and the
     * typing indicator could be shown for 10 seconds even if the message was sent already.
     * <p>
     * Any occurring exceptions including ratelimit exceptions are given to the provided {@code exceptionHandler} or
     * ignored if it is {@code null}.
     *
     * @param exceptionHandler The handler that exceptions are given to.
     * @return An auto-closable to stop sending the typing indicator.
     * @see #type()
     * @see #typeContinuously()
     * @see #typeContinuouslyAfter(long, TimeUnit)
     * @see #typeContinuouslyAfter(long, TimeUnit, Consumer)
     */
    default NonThrowingAutoCloseable typeContinuously(Consumer<Throwable> exceptionHandler) {
        return typeContinuouslyAfter(0, TimeUnit.NANOSECONDS, exceptionHandler);
    }

    /**
     * Displays the "xyz is typing..." message continuously, starting delayed.
     * The message is continuously displayed if not quit using the returned {@code AutoCloseable}.
     * Sending a message will make the message go away shortly, but it will return immediately if not cancelled using
     * the {@code AutoCloseable}. This can be used in a try-with-resources block like
     * <code>try (NonThrowingAutoCloseable typingIndicator = textChannel.typeContinuouslyAfter(500,
     * TimeUnit.MILLISECONDS, ((Function&lt;Throwable, ?&gt;) Javacord::exceptionLogger)::apply))
     * { /* do lengthy stuff &#42;/ } sendReply();</code>.
     * <p>
     * The typing indicator will be shown delayed. This can be useful if the task you do can be finished in very short
     * time which could cause the typing indicator and the response message being sent at the same time and the typing
     * indicator could be shown for 10 seconds even if the message was sent already. With the delay this is
     * compensated, because if the returned {@code AutoCloseable} is closed before the delay is over, no typing
     * indicator will be sent at all.
     * <p>
     * Any occurring exceptions including ratelimit exceptions are given to the provided {@code exceptionHandler} or
     * ignored if it is {@code null}.
     *
     * @param exceptionHandler The handler that exceptions are given to.
     * @param delay The delay to wait until the first typing indicator is sent.
     * @param timeUnit The time unit of the delay value.
     * @return An auto-closable to stop sending the typing indicator.
     * @see #type()
     * @see #typeContinuously()
     * @see #typeContinuously(Consumer)
     * @see #typeContinuouslyAfter(long, TimeUnit)
     */
    default NonThrowingAutoCloseable typeContinuouslyAfter(
            long delay, TimeUnit timeUnit, Consumer<Throwable> exceptionHandler) {
        // the delegate that does the actual type indicator sending and error handling
        Runnable typeRunnable = () -> {
            CompletableFuture<?> typeFuture = type();
            if (exceptionHandler != null) {
                typeFuture.exceptionally(throwable -> {
                    exceptionHandler.accept(throwable);
                    return null;
                });
            }
        };

        DiscordApi api = getApi();

        // schedule regular type indicator sending
        Future<?> typingIndicator = api.getThreadPool().getScheduler().scheduleWithFixedDelay(
                typeRunnable, TimeUnit.NANOSECONDS.convert(delay, timeUnit), 8_000_000_000L, TimeUnit.NANOSECONDS);

        // prevent messages from other commands to interrupt the typing indicator too long
        ListenerManager<MessageCreateListener> typingInterruptedListenerManager =
                api.addMessageCreateListener(event -> {
                    if (event.getMessage().getAuthor().isYourself()) {
                        typeRunnable.run();
                    }
                });

        // auto-closable to cancel the continuously typing indicator
        return  () -> {
            typingInterruptedListenerManager.remove();
            typingIndicator.cancel(true);
        };
    }

    /**
     * Deletes multiple messages at once.
     * Any message given that is invalid will count towards the minimum and maximum message count
     * (currently 2 and 100 respectively). Additionally, duplicated messages will only be counted once.
     * If a message is older than 2 weeks, the method will fail.
     *
     * @param messages The messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> bulkDelete(Iterable<Message> messages) {
        return bulkDelete(StreamSupport.stream(messages.spliterator(), false).mapToLong(Message::getId).toArray());
    }

    /**
     * Deletes multiple messages at once.
     * Any message id given that do not exist or are invalid will count towards the minimum and maximum message count
     * (currently 2 and 100 respectively). Additionally, duplicated ids will only be counted once.
     * If a message is older than 2 weeks, the method will fail.
     *
     * @param messageIds The ids of the messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> bulkDelete(long... messageIds) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        ArrayNode messages = body.putArray("messages");
        LongStream.of(messageIds).boxed()
                .map(String::valueOf)
                .forEach(messages::add);

        return new RestRequest<Void>(getApi(), RestMethod.POST, RestEndpoint.MESSAGES_BULK_DELETE)
                .setRatelimitRetries(0)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(body)
                .execute(result -> null);
    }

    /**
     * Deletes multiple messages at once.
     * Any message id given that do not exist or are invalid will count towards the minimum and maximum message count
     * (currently 2 and 100 respectively). Additionally, duplicated ids will only be counted once.
     * If a message is older than 2 weeks, the method will fail.
     *
     * @param messageIds The ids of the messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> bulkDelete(String... messageIds) {
        long[] messageLongIds = Arrays.asList(messageIds).stream().filter(s -> {
            try {
                //noinspection ResultOfMethodCallIgnored
                Long.parseLong(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }).mapToLong(Long::parseLong).toArray();
        return bulkDelete(messageLongIds);
    }

    /**
     * Deletes multiple messages at once.
     * Any message given that is invalid will count towards the minimum and maximum message count
     * (currently 2 and 100 respectively). Additionally, duplicated messages will only be counted once.
     * If a message is older than 2 weeks, the method will fail.
     *
     * @param messages The messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> bulkDelete(Message... messages) {
        return bulkDelete(Arrays.asList(messages).stream().mapToLong(Message::getId).toArray());
    }

    /**
     * Gets a message by its id.
     *
     * @param id The id of the message.
     * @return The message with the given id.
     */
    default CompletableFuture<Message> getMessageById(long id) {
        return getApi().getCachedMessageById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new RestRequest<Message>(getApi(), RestMethod.GET, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getId()), String.valueOf(id))
                .execute(result -> ((ImplDiscordApi) getApi()).getOrCreateMessage(this, result.getJsonBody())));
    }

    /**
     * Gets a message by its id.
     *
     * @param id The id of the message.
     * @return The message with the given id.
     */
    default CompletableFuture<Message> getMessageById(String id) {
        try {
            return getMessageById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return getMessageById(-1);
        }
    }

    /**
     * Gets a list with all pinned messages.
     *
     * @return A list with all pinned messages.
     */
    default CompletableFuture<MessageSet> getPins() {
        return new RestRequest<MessageSet>(getApi(), RestMethod.GET, RestEndpoint.PINS)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<Message> pins = StreamSupport.stream(result.getJsonBody().spliterator(), false)
                            .map(pinJson -> ((ImplDiscordApi) getApi()).getOrCreateMessage(this, pinJson))
                            .collect(Collectors.toList());
                    return new ImplMessageSet(pins);
                });
    }

    /**
     * Gets up to a given amount of messages in this channel from the newer end.
     *
     * @param limit The limit of messages to get.
     * @return The messages.
     * @see #getMessagesAsStream()
     */
    default CompletableFuture<MessageSet> getMessages(int limit) {
        return ImplMessageSet.getMessages(this, limit);
    }

    /**
     * Gets messages in this channel from the newer end until one that meets the given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @return The messages.
     * @see #getMessagesAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesUntil(Predicate<Message> condition) {
        return ImplMessageSet.getMessagesUntil(this, condition);
    }

    /**
     * Gets messages in this channel from the newer end while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @return The messages.
     * @see #getMessagesAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesWhile(Predicate<Message> condition) {
        return ImplMessageSet.getMessagesWhile(this, condition);
    }

    /**
     * Gets a stream of messages in this channel sorted from newest to oldest.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @return The stream.
     * @see #getMessages(int)
     */
    default Stream<Message> getMessagesAsStream() {
        return ImplMessageSet.getMessagesAsStream(this);
    }

    /**
     * Gets up to a given amount of messages in this channel before a given message in any channel.
     *
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @return The messages.
     * @see #getMessagesBeforeAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBefore(int limit, long before) {
        return ImplMessageSet.getMessagesBefore(this, limit, before);
    }

    /**
     * Gets messages in this channel before a given message in any channel until one that meets the given condition is
     * found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param before Get messages before the message with this id.
     * @return The messages.
     * @see #getMessagesBeforeAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBeforeUntil(Predicate<Message> condition, long before) {
        return ImplMessageSet.getMessagesBeforeUntil(this, condition, before);
    }

    /**
     * Gets messages in this channel before a given message in any channel while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @param before Get messages before the message with this id.
     * @return The messages.
     * @see #getMessagesBeforeAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBeforeWhile(Predicate<Message> condition, long before) {
        return ImplMessageSet.getMessagesBeforeWhile(this, condition, before);
    }

    /**
     * Gets a stream of messages in this channel before a given message in any channel sorted from newest to oldest.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param before Get messages before the message with this id.
     * @return The stream.
     * @see #getMessagesBefore(int, long)
     */
    default Stream<Message> getMessagesBeforeAsStream(long before) {
        return ImplMessageSet.getMessagesBeforeAsStream(this, before);
    }

    /**
     * Gets up to a given amount of messages in this channel before a given message in any channel.
     *
     * @param limit The limit of messages to get.
     * @param before Get messages before this message.
     * @return The messages.
     * @see #getMessagesBeforeAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesBefore(int limit, Message before) {
        return getMessagesBefore(limit, before.getId());
    }

    /**
     * Gets messages in this channel before a given message in any channel until one that meets the given condition is
     * found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param before Get messages before this message.
     * @return The messages.
     * @see #getMessagesBeforeAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesBeforeUntil(Predicate<Message> condition, Message before) {
        return getMessagesBeforeUntil(condition, before.getId());
    }

    /**
     * Gets messages in this channel before a given message in any channel while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @param before Get messages before this message.
     * @return The messages.
     * @see #getMessagesBeforeAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesBeforeWhile(Predicate<Message> condition, Message before) {
        return getMessagesBeforeWhile(condition, before.getId());
    }

    /**
     * Gets a stream of messages in this channel before a given message in any channel sorted from newest to oldest.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param before Get messages before this message.
     * @return The stream.
     * @see #getMessagesBefore(int, Message)
     */
    default Stream<Message> getMessagesBeforeAsStream(Message before) {
        return getMessagesBeforeAsStream(before.getId());
    }

    /**
     * Gets up to a given amount of messages in this channel after a given message in any channel.
     *
     * @param limit The limit of messages to get.
     * @param after Get messages after the message with this id.
     * @return The messages.
     * @see #getMessagesAfterAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesAfter(int limit, long after) {
        return ImplMessageSet.getMessagesAfter(this, limit, after);
    }

    /**
     * Gets messages in this channel after a given message in any channel until one that meets the given condition is
     * found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param after Get messages after the message with this id.
     * @return The messages.
     * @see #getMessagesAfterAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesAfterUntil(Predicate<Message> condition, long after) {
        return ImplMessageSet.getMessagesAfterUntil(this, condition, after);
    }

    /**
     * Gets messages in this channel after a given message in any channel while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @param after Get messages after the message with this id.
     * @return The messages.
     * @see #getMessagesAfterAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesAfterWhile(Predicate<Message> condition, long after) {
        return ImplMessageSet.getMessagesAfterWhile(this, condition, after);
    }

    /**
     * Gets a stream of messages in this channel after a given message in any channel sorted from oldest to newest.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param after Get messages after the message with this id.
     * @return The messages.
     * @see #getMessagesAfter(int, long)
     */
    default Stream<Message> getMessagesAfterAsStream(long after) {
        return ImplMessageSet.getMessagesAfterAsStream(this, after);
    }

    /**
     * Gets up to a given amount of messages in this channel after a given message in any channel.
     *
     * @param limit The limit of messages to get.
     * @param after Get messages after this message.
     * @return The messages.
     * @see #getMessagesAfterAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesAfter(int limit, Message after) {
        return getMessagesAfter(limit, after.getId());
    }

    /**
     * Gets messages in this channel after a given message in any channel until one that meets the given condition is
     * found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param after Get messages after this message.
     * @return The messages.
     * @see #getMessagesAfterAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesAfterUntil(Predicate<Message> condition, Message after) {
        return getMessagesAfterUntil(condition, after.getId());
    }

    /**
     * Gets messages in this channel after a given message in any channel while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @param after Get messages after this message.
     * @return The messages.
     * @see #getMessagesAfterAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesAfterWhile(Predicate<Message> condition, Message after) {
        return getMessagesAfterWhile(condition, after.getId());
    }

    /**
     * Gets a stream of messages in this channel after a given message in any channel sorted from oldest to newest.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param after Get messages after this message.
     * @return The stream.
     * @see #getMessagesAfter(int, Message)
     */
    default Stream<Message> getMessagesAfterAsStream(Message after) {
        return getMessagesAfterAsStream(after.getId());
    }

    /**
     * Gets up to a given amount of messages in this channel around a given message in any channel.
     * The given message will be part of the result in addition to the messages around if it was sent in this channel
     * and does not count towards the limit.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param limit The limit of messages to get.
     * @param around Get messages around the message with this id.
     * @return The messages.
     * @see #getMessagesAroundAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesAround(int limit, long around) {
        return ImplMessageSet.getMessagesAround(this, limit, around);
    }

    /**
     * Gets messages in this channel around a given message in any channel until one that meets the given condition is
     * found. If no message matches the condition, an empty set is returned.
     * The given message will be part of the result in addition to the messages around if it was sent in this channel
     * and is matched against the condition and will abort retrieval.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param around Get messages around the message with this id.
     * @return The messages.
     * @see #getMessagesAroundAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesAroundUntil(Predicate<Message> condition, long around) {
        return ImplMessageSet.getMessagesAroundUntil(this, condition, around);
    }

    /**
     * Gets messages in this channel around a given message in any channel while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     * The given message will be part of the result in addition to the messages around if it was sent in this channel
     * and is matched against the condition and will abort retrieval.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param condition The condition that has to be met.
     * @param around Get messages around the message with this id.
     * @return The messages.
     * @see #getMessagesAroundAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesAroundWhile(Predicate<Message> condition, long around) {
        return ImplMessageSet.getMessagesAroundWhile(this, condition, around);
    }

    /**
     * Gets a stream of messages in this channel around a given message in any channel.
     * The first message in the stream will be the given message if it was sent in this channel.
     * After that you will always get an older message and a newer message alternating as long as on both sides
     * messages are available. If only on one side further messages are available, only those are delivered further on.
     * It's not guaranteed to be perfectly balanced.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param around Get messages around the message with this id.
     * @return The stream.
     * @see #getMessagesAround(int, long)
     */
    default Stream<Message> getMessagesAroundAsStream(long around) {
        return ImplMessageSet.getMessagesAroundAsStream(this, around);
    }

    /**
     * Gets up to a given amount of messages in this channel around a given message in any channel.
     * The given message will be part of the result in addition to the messages around if it was sent in this channel
     * and does not count towards the limit.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param limit The limit of messages to get.
     * @param around Get messages around this message.
     * @return The messages.
     * @see #getMessagesAroundAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesAround(int limit, Message around) {
        return getMessagesAround(limit, around.getId());
    }

    /**
     * Gets messages in this channel around a given message in any channel until one that meets the given condition is
     * found. If no message matches the condition, an empty set is returned.
     * The given message will be part of the result in addition to the messages around if it was sent in this channel
     * and is matched against the condition and will abort retrieval.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param around Get messages around this message.
     * @return The messages.
     * @see #getMessagesAroundAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesAroundUntil(Predicate<Message> condition, Message around) {
        return getMessagesAroundUntil(condition, around.getId());
    }

    /**
     * Gets messages in this channel around a given message in any channel while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     * The given message will be part of the result in addition to the messages around if it was sent in this channel
     * and is matched against the condition and will abort retrieval.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param condition The condition that has to be met.
     * @param around Get messages around this message.
     * @return The messages.
     * @see #getMessagesAroundAsStream(Message)
     */
    default CompletableFuture<MessageSet> getMessagesAroundWhile(Predicate<Message> condition, Message around) {
        return getMessagesAroundWhile(condition, around.getId());
    }

    /**
     * Gets a stream of messages in this channel around a given message in any channel.
     * The first message in the stream will be the given message if it was sent in this channel.
     * After that you will always get an older message and a newer message alternating as long as on both sides
     * messages are available. If only on one side further messages are available, only those are delivered further on.
     * It's not guaranteed to be perfectly balanced.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param around Get messages around this message.
     * @return The stream.
     * @see #getMessagesAround(int, Message)
     */
    default Stream<Message> getMessagesAroundAsStream(Message around) {
        return getMessagesAroundAsStream(around.getId());
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries.
     *
     * @param from The id of the start boundary messages.
     * @param to The id of the other boundary messages.
     * @return The messages.
     * @see #getMessagesBetweenAsStream(long, long)
     */
    default CompletableFuture<MessageSet> getMessagesBetween(long from, long to) {
        return ImplMessageSet.getMessagesBetween(this, from, to);
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries, until one that meets the given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param from The id of the start boundary messages.
     * @param to The id of the other boundary messages.
     * @return The messages.
     * @see #getMessagesBetweenAsStream(long, long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenUntil(Predicate<Message> condition, long from, long to) {
        return ImplMessageSet.getMessagesBetweenUntil(this, condition, from, to);
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries, while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @param from The id of the start boundary messages.
     * @param to The id of the other boundary messages.
     * @return The messages.
     * @see #getMessagesBetweenAsStream(long, long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenWhile(Predicate<Message> condition, long from, long to) {
        return ImplMessageSet.getMessagesBetweenWhile(this, condition, from, to);
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries, sorted from first given message to the second given message.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param from The id of the start boundary messages.
     * @param to The id of the other boundary messages.
     * @return The stream.
     * @see #getMessagesBetween(long, long)
     */
    default Stream<Message> getMessagesBetweenAsStream(long from, long to) {
        return ImplMessageSet.getMessagesBetweenAsStream(this, from, to);
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries.
     *
     * @param from The start boundary messages.
     * @param to The other boundary messages.
     * @return The messages.
     * @see #getMessagesBetweenAsStream(long, long)
     */
    default CompletableFuture<MessageSet> getMessagesBetween(Message from, Message to) {
        return getMessagesBetween(from.getId(), to.getId());
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries, until one that meets the given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @param from The start boundary messages.
     * @param to The other boundary messages.
     * @return The messages.
     * @see #getMessagesBetweenAsStream(long, long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenUntil(
            Predicate<Message> condition, Message from, Message to) {
        return getMessagesBetweenUntil(condition, from.getId(), to.getId());
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries, while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @param from The start boundary messages.
     * @param to The other boundary messages.
     * @return The messages.
     * @see #getMessagesBetweenAsStream(long, long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenWhile(
            Predicate<Message> condition, Message from, Message to) {
        return getMessagesBetweenWhile(condition, from.getId(), to.getId());
    }

    /**
     * Gets all messages in this channel between the first given message in any channel and the second given message in
     * any channel, excluding the boundaries, sorted from first given message to the second given message.
     * <p>
     * The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param from The start boundary messages.
     * @param to The other boundary messages.
     * @return The stream.
     * @see #getMessagesBetween(long, long)
     */
    default Stream<Message> getMessagesBetweenAsStream(Message from, Message to) {
        return getMessagesBetweenAsStream(from.getId(), to.getId());
    }

    /**
     * Gets the message cache for the channel.
     *
     * @return The message cache for the channel.
     */
    MessageCache getMessageCache();

    /**
     * Gets a list of all webhooks in this channel.
     *
     * @return A list of all webhooks in this channel.
     */
    default CompletableFuture<List<Webhook>> getWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), RestMethod.GET, RestEndpoint.CHANNEL_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<Webhook> webhooks = new ArrayList<>();
                    for (JsonNode webhook : result.getJsonBody()) {
                        webhooks.add(new ImplWebhook(getApi(), webhook));
                    }
                    return Collections.unmodifiableList(webhooks);
                });
    }

    /**
     * Checks if the given user can send messages in this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     *
     * @param user The user to check.
     * @return Whether the given user can write messages or not.
     */
    default boolean canWrite(User user) {
        Optional<PrivateChannel> privateChannel = asPrivateChannel();
        if (privateChannel.isPresent()) {
            return user.isYourself() || privateChannel.get().getRecipient() == user;
        }
        Optional<GroupChannel> groupChannel = asGroupChannel();
        if (groupChannel.isPresent()) {
            return user.isYourself() || groupChannel.get().getMembers().contains(user);
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasPermission(user, PermissionType.ADMINISTRATOR)
                || severTextChannel.get()
                .hasPermissions(user, PermissionType.READ_MESSAGES, PermissionType.SEND_MESSAGES);
    }

    /**
     * Checks if the user of the connected account can send messages in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     *
     * @return Whether the user of the connected account can write messages or not.
     */
    default boolean canYouWrite() {
        return canWrite(getApi().getYourself());
    }

    /**
     * Checks if the given user can use external emojis in this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     * It also doesn't check if the user is even able to send any external emojis (twitch subscription or nitro).
     *
     * @param user The user to check.
     * @return Whether the given user can use external emojis or not.
     */
    default boolean canUseExternalEmojis(User user) {
        if (!canWrite(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasAnyPermission(user,
                                                           PermissionType.ADMINISTRATOR,
                                                           PermissionType.USE_EXTERNAL_EMOJIS);
    }

    /**
     * Checks if the user of the connected account can use external emojis in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     * It also doesn't check if the user is even able to send any external emojis (twitch subscription or nitro).
     *
     * @return Whether the user of the connected account can use external emojis or not.
     */
    default boolean canYouUseExternalEmojis() {
        return canUseExternalEmojis(getApi().getYourself());
    }

    /**
     * Checks if the given user can use embed links in this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     *
     * @param user The user to check.
     * @return Whether the given user can embed links or not.
     */
    default boolean canEmbedLinks(User user) {
        if (!canWrite(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasAnyPermission(user,
                                                           PermissionType.ADMINISTRATOR,
                                                           PermissionType.EMBED_LINKS);
    }

    /**
     * Checks if the user of the connected account can use embed links in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     *
     * @return Whether the user of the connected account can embed links or not.
     */
    default boolean canYouEmbedLinks() {
        return canEmbedLinks(getApi().getYourself());
    }

    /**
     * Checks if the given user can read the message history of this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can read the message history or not.
     */
    default boolean canReadMessageHistory(User user) {
        if (!canSee(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasAnyPermission(user,
                                                           PermissionType.ADMINISTRATOR,
                                                           PermissionType.READ_MESSAGE_HISTORY);
    }

    /**
     * Checks if the user of the connected account can read the message history of this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can read the message history or not.
     */
    default boolean canYouReadMessageHistory() {
        return canReadMessageHistory(getApi().getYourself());
    }

    /**
     * Checks if the given user can use tts (text to speech) in this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     *
     * @param user The user to check.
     * @return Whether the given user can use tts or not.
     */
    default boolean canUseTts(User user) {
        if (!canWrite(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasAnyPermission(user,
                                                           PermissionType.ADMINISTRATOR,
                                                           PermissionType.SEND_TTS_MESSAGES);
    }

    /**
     * Checks if the user of the connected account can use tts (text to speech) in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     * Please notice, this does not check if a user has blocked private messages!
     *
     * @return Whether the user of the connected account can use tts or not.
     */
    default boolean canYouUseTts() {
        return canUseTts(getApi().getYourself());
    }

    /**
     * Checks if the given user can attach files in this channel.
     *
     * @param user The user to check.
     * @return Whether the given user can attach files or not.
     */
    default boolean canAttachFiles(User user) {
        Optional<PrivateChannel> privateChannel = asPrivateChannel();
        if (privateChannel.isPresent()) {
            return user.isYourself() || privateChannel.get().getRecipient() == user;
        }
        Optional<GroupChannel> groupChannel = asGroupChannel();
        if (groupChannel.isPresent()) {
            return user.isYourself() || groupChannel.get().getMembers().contains(user);
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasPermission(user, PermissionType.ADMINISTRATOR)
                || (severTextChannel.get().hasPermission(user, PermissionType.ATTACH_FILE)
                && severTextChannel.get().canWrite(user));
    }

    /**
     * Checks if the user of the connected account can attach files in this channel.
     *
     * @return Whether the user of the connected account can attach files or not.
     */
    default boolean canYouAttachFiles() {
        return canAttachFiles(getApi().getYourself());
    }

    /**
     * Checks if the given user is allowed to add <b>new</b> reactions to messages in this channel.
     *
     * @param user The user to check.
     * @return Whether the given user is allowed to add <b>new</b> reactions to messages in this channel or not.
     */
    default boolean canAddNewReactions(User user) {
        Optional<PrivateChannel> privateChannel = asPrivateChannel();
        if (privateChannel.isPresent()) {
            return user.isYourself() || privateChannel.get().getRecipient() == user;
        }
        Optional<GroupChannel> groupChannel = asGroupChannel();
        if (groupChannel.isPresent()) {
            return user.isYourself() || groupChannel.get().getMembers().contains(user);
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasPermission(user, PermissionType.ADMINISTRATOR)
                || severTextChannel.get().hasPermissions(user,
                PermissionType.READ_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.ADD_REACTIONS);
    }

    /**
     * Checks if the user of the connected account is allowed to add <b>new</b> reactions to messages in this channel.
     *
     * @return Whether the user of the connected account is allowed to add <b>new</b> reactions to messages in this channel or not.
     */
    default boolean canYouAddNewReactions() {
        return canAddNewReactions(getApi().getYourself());
    }

    /**
     * Checks if the given user can manage messages (delete or pin them or remove reactions of others) in this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can manage messages or not.
     */
    default boolean canManageMessages(User user) {
        if (!canSee(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasAnyPermission(user,
                                                           PermissionType.ADMINISTRATOR,
                                                           PermissionType.MANAGE_MESSAGES);
    }

    /**
     * Checks if the user of the connected account can manage messages (delete or pin them or remove reactions of others) in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can manage messages or not.
     */
    default boolean canYouManageMessages() {
        return canManageMessages(getApi().getYourself());
    }

    /**
     * Checks if the given user can remove reactions of other users in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     * This method just forwards to {@link #canManageMessages(User)} as it is the same permission that is needed.
     *
     * @param user The user to check.
     * @return Whether the given user can remove reactions of others or not.
     */
    default boolean canRemoveReactionsOfOthers(User user) {
        return canManageMessages(user);
    }

    /**
     * Checks if the user of the connected account can remove reactions of other users in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can remove reactions of others or not.
     */
    default boolean canYouRemoveReactionsOfOthers() {
        return canRemoveReactionsOfOthers(getApi().getYourself());
    }

    /**
     * Checks if the given user can mention everyone (@everyone) in this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can mention everyone (@everyone) or not.
     */
    default boolean canMentionEveryone(User user) {
        if (!canSee(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasPermission(user, PermissionType.ADMINISTRATOR)
                || (severTextChannel.get().hasPermission(user, PermissionType.MENTION_EVERYONE)
                && severTextChannel.get().canWrite(user));
    }

    /**
     * Checks if the user of the connected account can mention everyone (@everyone) in this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can mention everyone (@everyone) or not.
     */
    default boolean canYouMentionEveryone() {
        return canMentionEveryone(getApi().getYourself());
    }

    /**
     * Adds a listener, which listens to message creates in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                TextChannel.class, getId(), MessageCreateListener.class, listener);
    }

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    default List<MessageCreateListener> getMessageCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(TextChannel.class, getId(), MessageCreateListener.class);
    }

    /**
     * Adds a listener, which listens to users starting to type in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                TextChannel.class, getId(), UserStartTypingListener.class, listener);
    }

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    default List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                TextChannel.class, getId(), UserStartTypingListener.class);
    }

    /**
     * Adds a listener, which listens to message deletions in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                TextChannel.class, getId(), MessageDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered message delete listeners.
     *
     * @return A list with all registered message delete listeners.
     */
    default List<MessageDeleteListener> getMessageDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(TextChannel.class, getId(), MessageDeleteListener.class);
    }

    /**
     * Adds a listener, which listens to message edits in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(TextChannel.class, getId(), MessageEditListener.class, listener);
    }

    /**
     * Gets a list with all registered message edit listeners.
     *
     * @return A list with all registered message edit listeners.
     */
    default List<MessageEditListener> getMessageEditListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(TextChannel.class, getId(), MessageEditListener.class);
    }

    /**
     * Adds a listener, which listens to reactions being added in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(TextChannel.class, getId(), ReactionAddListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    default List<ReactionAddListener> getReactionAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(TextChannel.class, getId(), ReactionAddListener.class);
    }

    /**
     * Adds a listener, which listens to reactions being removed in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                TextChannel.class, getId(), ReactionRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(TextChannel.class, getId(), ReactionRemoveListener.class);
    }

    /**
     * Adds a listener, which listens to all reactions being removed at once from a message in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            ReactionRemoveAllListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(TextChannel.class, getId(), ReactionRemoveAllListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction remove all listeners.
     *
     * @return A list with all registered reaction remove all listeners.
     */
    default List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(TextChannel.class, getId(), ReactionRemoveAllListener.class);
    }

    /**
     * Adds a listener, which listens to all pin updates in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(
            ChannelPinsUpdateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(TextChannel.class, getId(), ChannelPinsUpdateListener.class, listener);
    }

    /**
     * Gets a list with all registered channel pins update listeners.
     *
     * @return A list with all registered channel pins update listeners.
     */
    default List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(TextChannel.class, getId(), ChannelPinsUpdateListener.class);
    }

    /**
     * Adds a listener, which listens to all cached message pins in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(TextChannel.class, getId(), CachedMessagePinListener.class, listener);
    }

    /**
     * Gets a list with all registered cached message pin listeners.
     *
     * @return A list with all registered cached message pin listeners.
     */
    default List<CachedMessagePinListener> getCachedMessagePinListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(TextChannel.class, getId(), CachedMessagePinListener.class);
    }

    /**
     * Adds a listener, which listens to all cached message unpins in this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(
            CachedMessageUnpinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(TextChannel.class, getId(), CachedMessageUnpinListener.class, listener);
    }

    /**
     * Gets a list with all registered cached message unpin listeners.
     *
     * @return A list with all registered cached message unpin listeners.
     */
    default List<CachedMessageUnpinListener> getCachedMessageUnpinListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(TextChannel.class, getId(), CachedMessageUnpinListener.class);
    }

    /**
     * Adds a listener that implements one or more {@code TextChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
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
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(TextChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code TextChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
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
                        ((ImplDiscordApi) getApi()).removeObjectListener(TextChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code TextChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code TextChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    default <T extends TextChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getTextChannelAttachableListeners() {
        Map<T, List<Class<T>>> textChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(TextChannel.class, getId());
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> textChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return textChannelListeners;
    }

    /**
     * Removes a listener from this text channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends TextChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(TextChannel.class, getId(), listenerClass, listener);
    }

    @Override
    default Optional<? extends TextChannel> getCurrentCachedInstance() {
        return getApi().getTextChannelById(getId());
    }

    @Override
    default CompletableFuture<? extends TextChannel> getLatestInstance() {
        Optional<? extends TextChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<? extends TextChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}
