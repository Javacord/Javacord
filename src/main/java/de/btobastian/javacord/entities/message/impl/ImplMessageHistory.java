/*
 * Copyright (C) 2016 Bastian Oppermann
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
package de.btobastian.javacord.entities.message.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.exceptions.PermissionsException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of the message history interface.
 */
public class ImplMessageHistory implements MessageHistory {

    private final ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of this class.
     *
     * @param api The used api.
     * @param channelId The id of the channel.
     * @param limit The maximum number of messages.
     * @throws Exception if something went wrong.
     */
    public ImplMessageHistory(ImplDiscordAPI api, String channelId, int limit) throws Exception {
        this(api, channelId, null, false, limit);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param api The used api.
     * @param channelId The id of the channel.
     * @param messageId Gets the messages before or after the message with the given id.
     * @param before Whether it should get the messages before or after the given message.
     * @param limit The maximum number of messages.
     * @throws Exception if something went wrong.
     */
    public ImplMessageHistory(ImplDiscordAPI api, String channelId, String messageId, boolean before, int limit)
            throws Exception {
        String link = messageId == null ?
                "https://discordapp.com/api/channels/" + channelId + "/messages?&limit=" + limit
                : "https://discordapp.com/api/channels/" + channelId + "/messages?&"
                + (before ? "before" : "after") + "=" + messageId + "&limit=" + limit;
        HttpResponse<JsonNode> response = Unirest.get(link).header("authorization", api.getToken()).asJson();
        api.checkResponse(response);
        JSONArray messages = response.getBody().getArray();
        for (int i = 0; i < messages.length(); i++) {
            JSONObject messageJson = messages.getJSONObject(i);
            String id = messageJson.getString("id");
            Message message = api.getMessageById(id);
            if (message == null) {
                message = new ImplMessage(messageJson, api, null);
            }
            this.messages.put(id, message);
        }
    }

    @Override
    public Message getMessageById(String id) {
        return messages.get(id);
    }

    @Override
    public Iterator<Message> iterator() {
        return messages.values().iterator();
    }

    /**
     * Removes the message with the given id.
     *
     * @param id The id of the message to remove.
     */
    public void removeMessage(String id) {
        messages.remove(id);
    }

}
