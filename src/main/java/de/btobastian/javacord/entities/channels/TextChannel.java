package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.impl.ImplMessageHistory;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.cache.MessageCache;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a text channel.
 */
public interface TextChannel extends Channel, Messageable {

    @Override
    default CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce) {
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
        return new RestRequest<Message>(getApi(), HttpMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(body)
                .execute(res -> ((ImplDiscordApi) getApi()).getOrCreateMessage(this, res.getBody().getObject()));
    }

    /**
     * Displays the "xyz is typing..." message.
     * The message automatically disappears after 5 seconds or after sending a message.
     *
     * @return A future to check for exceptions.
     */
    default CompletableFuture<Void> type() {
        return new RestRequest<Void>(getApi(), HttpMethod.POST, RestEndpoint.CHANNEL_TYPING)
                .setRatelimitRetries(0)
                .setUrlParameters(String.valueOf(getId()))
                .execute(res -> null);
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
     * Adds a listener, which listens to message creates in this channel.
     *
     * @param listener The listener to add.
     */
    void addMessageCreateListener(MessageCreateListener listener);

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    List<MessageCreateListener> getMessageCreateListeners();

    /**
     * Adds a listener, which listens to users starting to type in this channel.
     *
     * @param listener The listener to add.
     */
    void addUserStartTypingListener(UserStartTypingListener listener);

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    List<UserStartTypingListener> getUserStartTypingListeners();

    /**
     * Adds a listener, which listens to message deletions in this channel.
     *
     * @param listener The listener to add.
     */
    void addMessageDeleteListener(MessageDeleteListener listener);

    /**
     * Gets a list with all registered message delete listeners.
     *
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners();

    /**
     * Adds a listener, which listens to message edits in this channel.
     *
     * @param listener The listener to add.
     */
    void addMessageEditListener(MessageEditListener listener);

    /**
     * Gets a list with all registered message edit listeners.
     *
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners();

    /**
     * Adds a listener, which listens to reactions being added in this channel.
     *
     * @param listener The listener to add.
     */
    void addReactionAddListener(ReactionAddListener listener);

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners();

    /**
     * Adds a listener, which listens to reactions being removed in this channel.
     *
     * @param listener The listener to add.
     */
    void addReactionRemoveListener(ReactionRemoveListener listener);

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners();

}
