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
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of the message history interface.
 */
public class ImplMessageHistory implements MessageHistory {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplMessageHistory.class);

    private final ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

    private Message oldestMessage = null;
    private Message newestMessage = null;

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
        int step = 0;
        if (messageId == null) {
            before = true;
        }
        logger.debug("Trying to get message history (channel id: {}, message id: {}, before: {}, limit: {}",
                channelId, messageId == null ? "none" : messageId, before, limit);
        for (int i = limit / 100; i > 0; i--) {
            int receivedMessages;
            if (step++ == 0) { // if it's the first iteration step use the normal parameters
                receivedMessages = request(api, channelId, messageId, before, 100);
            } else {
                // now use the oldest/newest message
                receivedMessages =
                        request(api, channelId, before ? oldestMessage.getId() : newestMessage.getId(), before, 100);
            }
            if (receivedMessages == 0) {
                return; // stop requesting
            }
        }
        if (step == 0) { // step == 0 means a limit less than 100
            request(api, channelId, messageId, before, limit % 100);
        } else { // request the rest
            request(api, channelId, before ? oldestMessage.getId() : newestMessage.getId(), before, limit % 100);
        }
        logger.debug("Got message history (channel id: {}, message id: {}, before: {}, limit: {}, amount: {}",
                channelId, messageId == null ? "none" : messageId, before, limit, messages.size());
    }

    /**
     * Requests messages.
     *
     * @param api The used api.
     * @param channelId The id of the channel.
     * @param messageId Gets the messages before or after the message with the given id.
     * @param before Whether it should get the messages before or after the given message.
     * @param limit The maximum number of messages.
     * @return The amount of requested messages.
     * @throws Exception if something went wrong.
     */
    private int request(ImplDiscordAPI api, String channelId, String messageId, boolean before, int limit)
            throws Exception {
        if (limit <= 0) {
            return 0;
        }
        logger.debug("Requesting part of message history (channel id: {}, message id: {}, before: {}, limit: {}",
                channelId, messageId == null ? "none" : messageId, before, limit);
        String link = messageId == null ?
                "https://discordapp.com/api/channels/" + channelId + "/messages?&limit=" + limit
                : "https://discordapp.com/api/channels/" + channelId + "/messages?&"
                + (before ? "before" : "after") + "=" + messageId + "&limit=" + limit;
        HttpResponse<JsonNode> response = Unirest.get(link).header("authorization", api.getToken()).asJson();
        api.checkResponse(response);
        api.checkRateLimit(response, RateLimitType.UNKNOWN, null);
        JSONArray messages = response.getBody().getArray();
        for (int i = 0; i < messages.length(); i++) {
            JSONObject messageJson = messages.getJSONObject(i);
            String id = messageJson.getString("id");
            Message message = api.getMessageById(id);
            if (message == null) {
                message = new ImplMessage(messageJson, api, null);
            }
            if (newestMessage == null || message.compareTo(newestMessage) > 0) {
                newestMessage = message;
            }
            if (oldestMessage == null || message.compareTo(oldestMessage) < 0) {
                oldestMessage = message;
            }
            this.messages.put(id, message);
        }
        return messages.length();
    }

    @Override
    public Message getMessageById(String id) {
        return messages.get(id);
    }

    @Override
    public Iterator<Message> iterator() {
        return getMessages().iterator();
    }

    @Override
    public Collection<Message> getMessages() {
        return Collections.unmodifiableCollection(messages.values());
    }

    @Override
    public Message getNewestMessage() {
        if (this.newestMessage != null) {
            return this.newestMessage;
        }
        Message newestMessage = null;
        for (Message message : messages.values()) {
            if (newestMessage == null) {
                newestMessage = message;
            } else if (message.compareTo(newestMessage) > 0) {
                newestMessage = message;
            }
        }
        return newestMessage;
    }

    @Override
    public Message getOldestMessage() {
        if (this.oldestMessage != null) {
            return this.oldestMessage;
        }
        Message oldestMessage = null;
        for (Message message : messages.values()) {
            if (oldestMessage == null) {
                oldestMessage = message;
            } else if (message.compareTo(oldestMessage) < 0) {
                oldestMessage = message;
            }
        }
        return oldestMessage;
    }

    @Override
    public List<Message> getMessagesSorted() {
        List<Message> messages = new ArrayList<>(this.messages.values());
        Collections.sort(messages);
        return messages;
    }

    /**
     * Removes the message with the given id.
     *
     * @param id The id of the message to remove.
     */
    public void removeMessage(String id) {
        messages.remove(id);
        if (newestMessage != null && newestMessage.getId().equals(id)) {
            newestMessage = null;
        }
        if (oldestMessage != null && oldestMessage.getId().equals(id)) {
            oldestMessage = null;
        }
    }

}
