package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
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
        CompletableFuture<Message> future = new CompletableFuture<>();
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
        new RestRequest(getApi(), HttpMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(body)
                .execute()
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        future.completeExceptionally(throwable);
                        return;
                    }
                    future.complete(new ImplMessage((ImplDiscordApi) getApi(), this, response.getBody().getObject()));
                });
        return future;
    }

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

}
