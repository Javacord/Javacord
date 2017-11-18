package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
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
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.cache.MessageCache;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
        JSONObject body = new JSONObject()
                .put("content", content == null ? "" : content)
                .put("tts", tts)
                .put("mentions", new String[0]);
        if (embed != null) {
            body.put("embed", embed.toJSONObject());
        }
        if (nonce != null) {
            body.put("nonce", nonce);
        }
        RestRequest<Message> request = new RestRequest<Message>(getApi(), HttpMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getId()));
        if (stream != null && fileName != null) {
            request.addField("file", stream, fileName);
            request.addField("payload_json", body.toString());
        } else {
            request.setBody(body);
        }

        return request.execute(res -> ((ImplDiscordApi) getApi()).getOrCreateMessage(this, res.getBody().getObject()));
    }

    /**
     * Displays the "xyz is typing..." message.
     * The message automatically disappears after 5 seconds or after sending a message.
     *
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> type() {
        return new RestRequest<Void>(getApi(), HttpMethod.POST, RestEndpoint.CHANNEL_TYPING)
                .setRatelimitRetries(0)
                .setUrlParameters(String.valueOf(getId()))
                .execute(res -> null);
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
        Collection<String> messageStringIds = LongStream.of(messageIds).boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
        return new RestRequest<Void>(getApi(), HttpMethod.POST, RestEndpoint.MESSAGES_BULK_DELETE)
                .setRatelimitRetries(0)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(new JSONObject().put("messages", messageStringIds))
                .execute(res -> null);
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
     * Gets a message by it's id.
     *
     * @param id The id of the message.
     * @return The message with the given id.
     */
    default CompletableFuture<Message> getMessageById(long id) {
        return getApi().getCachedMessageById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new RestRequest<Message>(getApi(), HttpMethod.GET, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getId()), String.valueOf(id))
                .execute(res -> ((ImplDiscordApi) getApi()).getOrCreateMessage(this, res.getBody().getObject())));
    }

    /**
     * Gets a message by it's id.
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
        return new RestRequest<List<Message>>(getApi(), HttpMethod.GET, RestEndpoint.PINS)
                .setUrlParameters(getIdAsString())
                .execute(res -> {
                    List<Message> pins = new ArrayList<>();
                    JSONArray pinsJson = res.getBody().getArray();
                    for (int i = 0; i < pinsJson.length(); i++) {
                        pins.add(((ImplDiscordApi) getApi()).getOrCreateMessage(this, pinsJson.getJSONObject(i)));
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
     * Checks if the given user is allowed to add <b>new</b> reactions to messages in this channel.
     *
     * @param user The user to check.
     * @return Whether the user is allowed to add <b>new</b> reactions to message in this channel or not.
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
                || severTextChannel.get().hasPermissions(user, PermissionType.ADMINISTRATOR)
                || severTextChannel.get().hasPermissions(user,
                PermissionType.READ_MESSAGES,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.ADD_REACTIONS);
    }

    /**
     * Gets a list of all webhooks in this channel.
     *
     * @return A list of all webhooks in this channel.
     */
    default CompletableFuture<List<Webhook>> getWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), HttpMethod.GET, RestEndpoint.CHANNEL_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(res -> {
                    List<Webhook> webhooks = new ArrayList<>();
                    JSONArray webhooksJson = res.getBody().getArray();
                    for (int i = 0; i < webhooksJson.length(); i++) {
                        webhooks.add(new ImplWebhook(getApi(), webhooksJson.getJSONObject(i)));
                    }
                    return webhooks;
                });
    }

    /**
     * Adds a listener, which listens to message creates in this channel.
     *
     * @param listener The listener to add.
     */
    default void addMessageCreateListener(MessageCreateListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
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
     */
    default void addUserStartTypingListener(UserStartTypingListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
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
     */
    default void addMessageDeleteListener(MessageDeleteListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
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
     */
    default void addMessageEditListener(MessageEditListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(TextChannel.class, getId(), MessageEditListener.class, listener);
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
     */
    default void addReactionAddListener(ReactionAddListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(TextChannel.class, getId(), ReactionAddListener.class, listener);
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
     */
    default void addReactionRemoveListener(ReactionRemoveListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
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

}
