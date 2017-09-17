package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
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

}
