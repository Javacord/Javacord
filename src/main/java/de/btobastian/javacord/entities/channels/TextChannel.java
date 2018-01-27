package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.Webhook;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.impl.ImplMessageHistory;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.cache.MessageCache;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.LongStream;

/**
 * This class represents a text channel.
 */
public interface TextChannel extends Channel, Messageable {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(TextChannel.class);

    @Override
    default CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, InputStream stream, String fileName) {
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("content", content == null ? "" : content)
                .put("tts", tts);
        body.putArray("mentions");
        if (embed != null) {
            embed.toJsonNode(body.putObject("embed"));
        }
        if (nonce != null) {
            body.put("nonce", nonce);
        }

        RestRequest<Message> request = new RestRequest<Message>(getApi(), RestMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getId()));
        if (stream != null && fileName != null) {
            byte[] bytes;
            try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = stream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                bytes = buffer.toByteArray();
                stream.close();
            } catch (IOException e) {
                CompletableFuture<Message> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }

            String mediaType = URLConnection.guessContentTypeFromName(fileName);
            if (mediaType == null) {
                mediaType = "application/octet-stream";
            }

            request.setMultipartBody(new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("payload_json", body.toString())
                    .addFormDataPart("file", fileName,
                            RequestBody.create(MediaType.parse(mediaType), bytes)
                    ).build());
        } else {
            request.setBody(body);
        }

        return request.execute(result -> ((ImplDiscordApi) getApi()).getOrCreateMessage(this, result.getJsonBody()));
    }

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
     * Displays the "xyz is typing..." message continuously.
     * The message is continuously displayed if not quit using the returned {@code AutoCloseable}.
     * Sending a message will make the message go away, but it will return after a short delay if not canceled using
     * the {@code AutoCloseable}.
     * This can be used in a try-with-resources block like
     * <code>try(AutoCloseable typingIndicator = textChannel.typeContinuously()) { /* do lengthy stuff &#42;/ }</code>.
     * Any occurring exceptions including ratelimit exceptions are suppressed.
     * If you want to handle exceptions, use {@link #typeContinuously(Consumer)}.
     *
     * @return An auto-closable to stop sending the typing indicator.
     * @see #type()
     * @see #typeContinuously(Consumer)
     */
    default AutoCloseable typeContinuously() {
        Future typingIndicator = getApi().getThreadPool().getScheduler()
                .scheduleWithFixedDelay(this::type, 0, 8, TimeUnit.SECONDS);
        return () -> typingIndicator.cancel(true);
    }

    /**
     * Displays the "xyz is typing..." message continuously.
     * The message is continuously displayed if not quit using the returned {@code AutoCloseable}.
     * Sending a message will make the message go away, but it will return after a short delay if not canceled using
     * the {@code AutoCloseable}.
     * Any occurring exceptions including ratelimit exceptions are given to the provided {@code exceptionHandler}.
     * This can be used in a try-with-resources block like
     * <code>try(AutoCloseable typingIndicator = textChannel.typeContinuously(((Function&lt;Throwable, ?&gt;)
     * Javacord::exceptionLogger)::apply)) { /* do lengthy stuff &#42;/ }</code>.
     *
     * @param exceptionHandler The handler that exceptions are given to.
     * @return An auto-closable to stop sending the typing indicator.
     * @see #type()
     * @see #typeContinuously()
     */
    default AutoCloseable typeContinuously(Consumer<Throwable> exceptionHandler) {
        Future typingIndicator = getApi().getThreadPool().getScheduler()
                .scheduleWithFixedDelay(() -> type().exceptionally(throwable -> {
                    exceptionHandler.accept(throwable);
                    return null;
                }), 0, 8, TimeUnit.SECONDS);
        return () -> typingIndicator.cancel(true);
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
        Collection<Long> messageIds = new HashSet<>();
        messages.forEach(message -> messageIds.add(message.getId()));
        return bulkDelete(messageIds.stream().mapToLong(value -> value).toArray());
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
    default CompletableFuture<List<Message>> getPins() {
        return new RestRequest<List<Message>>(getApi(), RestMethod.GET, RestEndpoint.PINS)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<Message> pins = new ArrayList<>();
                    for (JsonNode pin : result.getJsonBody()) {
                        pins.add(((ImplDiscordApi) getApi()).getOrCreateMessage(this, pin));
                    }
                    return pins;
                });
    }

    /**
     * Gets the history of messages in this channel.
     *
     * @param limit The limit of messages to get.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistory(int limit) {
        return ImplMessageHistory.getHistory(this, limit);
    }

    /**
     * Gets the history of messages before a given message in this channel.
     *
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistoryBefore(int limit, long before) {
        return ImplMessageHistory.getHistoryBefore(this, limit, before);
    }

    /**
     * Gets the history of messages before a given message in this channel.
     *
     * @param limit The limit of messages to get.
     * @param before Get messages before this message.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistoryBefore(int limit, Message before) {
        return getHistoryBefore(limit, before.getId());
    }

    /**
     * Gets the history of messages after a given message in this channel.
     *
     * @param limit The limit of messages to get.
     * @param after Get messages after the message with this id.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistoryAfter(int limit, long after) {
        return ImplMessageHistory.getHistoryAfter(this, limit, after);
    }

    /**
     * Gets the history of messages after a given message in this channel.
     *
     * @param limit The limit of messages to get.
     * @param after Get messages after this message.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistoryAfter(int limit, Message after) {
        return getHistoryAfter(limit, after.getId());
    }
    /**
     * Gets the history of messages around a given message in this channel.
     * Half of the message will be older than the given message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param limit The limit of messages to get.
     * @param around Get messages around the message with this id.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistoryAround(int limit, long around) {
        return ImplMessageHistory.getHistoryAround(this, limit, around);
    }

    /**
     * Gets the history of messages around a given message in this channel.
     * Half of the message will be older than the given message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param limit The limit of messages to get.
     * @param around Get messages around this message.
     * @return The history.
     */
    default CompletableFuture<MessageHistory> getHistoryAround(int limit, Message around) {
        return getHistoryAround(limit, around.getId());
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
                    return webhooks;
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

}
