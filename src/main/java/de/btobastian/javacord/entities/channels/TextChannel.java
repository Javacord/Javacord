/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a text channel.
 */
public interface TextChannel extends Channel, Messageable {

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

}
