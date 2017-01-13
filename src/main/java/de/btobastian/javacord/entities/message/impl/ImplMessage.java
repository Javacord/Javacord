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
package de.btobastian.javacord.entities.message.impl;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.FutureCallback;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.embed.impl.ImplEmbed;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listener.message.MessageDeleteListener;
import de.btobastian.javacord.listener.message.MessageEditListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The implementation of the user interface.
 */
public class ImplMessage implements Message {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplMessage.class);

    private static final ThreadLocal<SimpleDateFormat> TIMEZONE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> FORMAT_ALTERNATIVE = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> FORMAT_ALTERNATIVE_TWO = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        }
    };

    private final ImplDiscordAPI api;

    private final String id;
    private String content = null;
    private final boolean tts;
    private final User author;
    private final List<User> mentions = new ArrayList<>();
    private final List<Role> mentionedRoles = new ArrayList<>();
    private final MessageReceiver receiver;
    private final String channelId;
    private final List<MessageAttachment> attachments = new ArrayList<>();
    private final String nonce;
    private boolean mentionsEveryone;
    private boolean pinned;
    private boolean deleted = false;
    private Calendar creationDate = Calendar.getInstance();
    private final Collection<Embed> embeds = new ArrayList<>();
    private final List<Reaction> reactions = new ArrayList<>();

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param api  The api of this server.
     */
    public ImplMessage(JSONObject data, ImplDiscordAPI api, MessageReceiver receiver) {
        this.api = api;

        id = data.getString("id");
        if (data.has("content")) {
            content = data.getString("content");
        }
        tts = data.getBoolean("tts");
        mentionsEveryone = data.getBoolean("mention_everyone");
        pinned = data.getBoolean("pinned");

        if (data.has("timestamp")) {
            String time = data.getString("timestamp");
            Calendar calendar = Calendar.getInstance();
            try {
                //remove the nano seconds, rejoining on +. If the formatting changes then the string will remain the same
                String nanoSecondsRemoved = Joiner.on("+").join(time.split("\\d{3}\\+"));
                calendar.setTime(TIMEZONE_FORMAT.get().parse(nanoSecondsRemoved));
            } catch (ParseException timeZoneIgnored) {
                try {
                    calendar.setTime(FORMAT.get().parse(time.substring(0, time.length() - 9)));
                } catch (ParseException ignored) {
                    try {
                        calendar.setTime(FORMAT_ALTERNATIVE.get().parse(time.substring(0, time.length() - 9)));
                    } catch (ParseException ignored2) {
                        try {
                            calendar.setTime(FORMAT_ALTERNATIVE_TWO.get().parse(time.substring(0, time.length() - 9)));
                        } catch (ParseException e) {
                            logger.warn("Could not parse timestamp {}. Please contact the developer!", time, e);
                        }
                    }
                }
            }
            creationDate = calendar;
        }
        author = api.getOrCreateUser(data.getJSONObject("author"));

        try {
            JSONArray attachments = data.getJSONArray("attachments");
            for (int i = 0; i < attachments.length(); i++) {
                JSONObject attachment = attachments.getJSONObject(i);
                String url = attachment.getString("url");
                String proxyUrl = attachment.getString("proxy_url");
                int size = attachment.getInt("size");
                String id = attachment.getString("id");
                String name = attachment.getString("filename");
                this.attachments.add(new ImplMessageAttachment(url, proxyUrl, size, id, name));
            }
        } catch (JSONException ignored) {
        }

        JSONArray mentions = data.getJSONArray("mentions");
        for (int i = 0; i < mentions.length(); i++) {
            String userId = mentions.getJSONObject(i).getString("id");
            User user;
            try {
                user = api.getUserById(userId).get();
            } catch (InterruptedException | ExecutionException e) {
                continue;
            }
            this.mentions.add(user);
        }

        JSONArray embeds = data.getJSONArray("embeds");
        for (int i = 0; i < embeds.length(); i++) {
            Embed embed = new ImplEmbed(embeds.getJSONObject(i));
            this.embeds.add(embed);
        }

        channelId = data.getString("channel_id");
        if (receiver == null) {
            this.receiver = findReceiver(channelId);
        } else {
            this.receiver = receiver;
        }

        if (data.has("reactions")) {
            JSONArray reactions = data.getJSONArray("reactions");
            for (int i = 0; i < reactions.length(); i++) {
                this.reactions.add(new ImplReaction(api, this, reactions.getJSONObject(i)));
            }
        }

        if (data.has("nonce") && !data.isNull("nonce")) {
            Object maybeItsAStringAndMaybeItsNotAStringIHaveNoClue = data.get("nonce");
            if (maybeItsAStringAndMaybeItsNotAStringIHaveNoClue instanceof String) {
                nonce = (String) maybeItsAStringAndMaybeItsNotAStringIHaveNoClue;
            } else {
                nonce = null;
            }
        } else {
            nonce = null;
        }

        if (getChannelReceiver() != null) {
            ImplServer server = (ImplServer) getChannelReceiver().getServer();
            server.addMember(author);

            JSONArray mentionRoles = data.getJSONArray("mention_roles");
            for (int i = 0; i < mentionRoles.length(); i++) {
                String roleId = mentionRoles.getString(i);
                Role role = server.getRoleById(roleId);
                if (role != null) {
                    this.mentionedRoles.add(role);
                }
            }
        }

        api.addMessage(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Channel getChannelReceiver() {
        if (receiver instanceof Channel) {
            return (Channel) receiver;
        }
        return null;
    }

    @Override
    public User getUserReceiver() {
        if (receiver instanceof User) {
            return (User) receiver;
        }
        return null;
    }

    @Override
    public MessageReceiver getReceiver() {
        return receiver;
    }

    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public boolean isPrivateMessage() {
        return getUserReceiver() != null;
    }

    @Override
    public List<User> getMentions() {
        return new ArrayList<>(mentions);
    }

    @Override
    public List<Role> getMentionedRoles() {
        return new ArrayList<>(mentionedRoles);
    }

    @Override
    public boolean isTts() {
        return tts;
    }

    @Override
    public String getNonce() {
        return nonce;
    }

    @Override
    public boolean isMentioningEveryone() {
        return mentionsEveryone;
    }

    @Override
    public boolean isPinned() {
        return pinned;
    }

    @Override
    public Future<Void> delete() {
        final ImplMessage message = this;
        return api.getThreadPool().getExecutorService().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                logger.debug("Trying to delete message (id: {}, author: {}, content: \"{}\")",
                        getId(), getAuthor(), getContent());
                if (isPrivateMessage()) {
                    api.checkRateLimit(null, RateLimitType.PRIVATE_MESSAGE_DELETE, null, null);
                } else {
                    api.checkRateLimit(null, RateLimitType.SERVER_MESSAGE_DELETE, null, getChannelReceiver());
                }
                HttpResponse<JsonNode> response = Unirest.delete
                        ("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId())
                        .header("authorization", api.getToken())
                        .asJson();
                api.checkResponse(response);
                if (isPrivateMessage()) {
                    api.checkRateLimit(response, RateLimitType.PRIVATE_MESSAGE_DELETE, null, null);
                } else {
                    api.checkRateLimit(
                            response, RateLimitType.SERVER_MESSAGE_DELETE, null, getChannelReceiver());
                }
                api.removeMessage(message);
                logger.debug("Deleted message (id: {}, author: {}, content: \"{}\")",
                        getId(), getAuthor(), getContent());
                synchronized (this) {
                    if (message.isDeleted()) {
                        return null;
                    } else {
                        message.setDeleted(true);
                    }
                }
                // call listener
                api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                    @Override
                    public void run() {
                        List<MessageDeleteListener> listeners = api.getListeners(MessageDeleteListener.class);
                        synchronized (listeners) {
                            for (MessageDeleteListener listener : listeners) {
                                try {
                                    listener.onMessageDelete(api, message);
                                } catch (Throwable t) {
                                    logger.warn("Uncaught exception in MessageDeleteListener!", t);
                                }
                            }
                        }
                    }
                });
                return null;
            }
        });
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public Collection<MessageAttachment> getAttachments() {
        return Collections.unmodifiableCollection(attachments);
    }

    @Override
    public Future<Message> reply(String content) {
        return receiver.sendMessage(content);
    }

    @Override
    public Future<Message> reply(String content, EmbedBuilder embed) {
        return receiver.sendMessage(content, embed);
    }

    @Override
    public Future<Message> reply(String content, FutureCallback<Message> callback) {
        return receiver.sendMessage(content, callback);
    }

    @Override
    public Future<Message> reply(String content, EmbedBuilder embed, FutureCallback<Message> callback) {
        return receiver.sendMessage(content, embed, callback);
    }

    @Override
    public Calendar getCreationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate.getTime());
        return calendar;
    }

    @Override
    public int compareTo(Message other) {
        return this.creationDate.compareTo(other.getCreationDate());
    }

    @Override
    public Future<Void> edit(final String content) {
        return api.getThreadPool().getExecutorService().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (isPrivateMessage()) {
                    api.checkRateLimit(null, RateLimitType.PRIVATE_MESSAGE, null, null);
                } else {
                    api.checkRateLimit(null, RateLimitType.SERVER_MESSAGE, null, getChannelReceiver());
                }
                HttpResponse<JsonNode> response = Unirest
                        .patch("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId())
                        .header("authorization", api.getToken())
                        .header("content-type", "application/json")
                        .body(new JSONObject().put("content", content).toString())
                        .asJson();
                api.checkResponse(response);
                if (isPrivateMessage()) {
                    api.checkRateLimit(response, RateLimitType.PRIVATE_MESSAGE, null, null);
                } else {
                    api.checkRateLimit(response, RateLimitType.SERVER_MESSAGE, null, getChannelReceiver());
                }
                final String oldContent = getContent();
                setContent(content);
                if (!oldContent.equals(content)) {
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<MessageEditListener> listeners = api.getListeners(MessageEditListener.class);
                            synchronized (listeners) {
                                for (MessageEditListener listener : listeners) {
                                    try {
                                        listener.onMessageEdit(api, ImplMessage.this, oldContent);
                                    } catch (Throwable t) {
                                        logger.warn("Uncaught exception in MessageEditListener!", t);
                                    }
                                }
                            }
                        }
                    });
                }
                return null;
            }
        });
    }

    @Override
    public Collection<Embed> getEmbeds() {
        return Collections.unmodifiableCollection(embeds);
    }

    @Override
    public Future<Void> addUnicodeReaction(final String unicodeEmoji) {
        return addReaction(unicodeEmoji);
    }

    @Override
    public Future<Void> addCustomEmojiReaction(CustomEmoji emoji) {
        return addReaction(emoji.getName() + ":" + emoji.getId());
    }

    @Override
    public List<Reaction> getReactions() {
        return new ArrayList<>(reactions);
    }

    @Override
    public Future<Void> removeAllReactions() {
        return api.getThreadPool().getExecutorService().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                logger.debug("Trying to remove all reactions from message {}", ImplMessage.this);
                HttpResponse<JsonNode> response = Unirest
                        .delete("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId() + "/reactions")
                        .header("authorization", api.getToken())
                        .asJson();
                api.checkResponse(response);
                if (isPrivateMessage()) {
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, null, null);
                } else {
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, null, getChannelReceiver());
                }
                logger.debug("Removed all reactions from message {}", ImplMessage.this);
                return null;
            }
        });
    }

    /**
     * Updates the content of the message.
     *
     * @param content The new content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets the deleted flag.
     *
     * @param deleted Whether the flag should be set to <code>true</code> or <code>false</code>.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Adds an unicode reaction to the cache.
     *
     * @param unicodeReaction The reaction to add.
     * @param you Whether the reaction was by you or not.
     * @return The reaction.
     */
    public Reaction addUnicodeReactionToCache(String unicodeReaction, boolean you) {
        for (Reaction reaction : reactions) {
            if (unicodeReaction.equals(reaction.getUnicodeEmoji())) {
                ((ImplReaction) reaction).incrementCount(you);
                return reaction;
            }
        }

        Reaction reaction = new ImplReaction(api, this, you, 1, unicodeReaction, null);
        reactions.add(reaction);
        return reaction;
    }

    /**
     * Adds an unicode reaction to the cache.
     *
     * @param customEmoji The reaction to add.
     * @param you Whether the reaction was by you or not.
     * @return The reaction.
     */
    public Reaction addCustomEmojiReactionToCache(CustomEmoji customEmoji, boolean you) {
        for (Reaction reaction : reactions) {
            if (customEmoji == reaction.getCustomEmoji()) {
                ((ImplReaction) reaction).incrementCount(you);
                return reaction;
            }
        }

        Reaction reaction = new ImplReaction(api, this, you, 1, null, customEmoji);
        reactions.add(reaction);
        return reaction;
    }

    /**
     * Removes an unicode reaction to the cache.
     *
     * @param unicodeReaction The reaction to remove.
     * @param you Whether the reaction was by you or not.
     * @return The reaction.
     */
    public Reaction removeUnicodeReactionToCache(String unicodeReaction, boolean you) {
        for (Reaction reaction : reactions) {
            if (unicodeReaction.equals(reaction.getUnicodeEmoji())) {
                ((ImplReaction) reaction).decrementCount(you);
                if (reaction.getCount() == 0) {
                    reactions.remove(reaction);
                }
                return reaction;
            }
        }

        // Reaction was not cached
        return null;
    }

    /**
     * Removes an unicode reaction to the cache.
     *
     * @param customEmoji The reaction to remove.
     * @param you Whether the reaction was by you or not.
     * @return The reaction.
     */
    public Reaction removeCustomEmojiReactionToCache(CustomEmoji customEmoji, boolean you) {
        for (Reaction reaction : reactions) {
            if (customEmoji == reaction.getCustomEmoji()) {
                ((ImplReaction) reaction).decrementCount(you);
                if (reaction.getCount() == 0) {
                    reactions.remove(reaction);
                }
                return reaction;
            }
        }

        // Reaction was not cached
        return null;
    }

    /**
     * Removes all reactions from cache.
     */
    public void removeAllReactionsFromCache() {
        reactions.clear();
    }

    /**
     * Gets the channel id of the message.
     *
     * @return The channel id of the message.
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Adds an reaction to the message.
     *
     * @param reaction The reaction to add. Whether a unicode emoji or a custom emoji in the format <code>name:id</code>.
     * @return A future which tells us if the creation was a success.
     */
    private Future<Void> addReaction(final String reaction) {
        return api.getThreadPool().getExecutorService().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                logger.debug("Trying to add reaction to message with id {} (reaction: {})", getId(), reaction);
                HttpResponse<JsonNode> response = Unirest
                        .put("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId() + "/reactions/" + reaction + "/@me")
                        .header("authorization", api.getToken())
                        .header("content-type", "application/json")
                        .body("{}")
                        .asJson();
                api.checkResponse(response);
                if (isPrivateMessage()) {
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, null, null);
                } else {
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, null, getChannelReceiver());
                }
                logger.debug("Added reaction to message with id {} (reaction: {})", getId(), reaction);
                return null;
            }
        });
    }

    /**
     * Tries to find the message receiver based on its channel id.
     *
     * @param channelId The channel id of the receiver.
     * @return The receiver with the given id.
     */
    private MessageReceiver findReceiver(String channelId) {
        for (Server server : api.getServers()) {
            if (server.getChannelById(channelId) != null) {
                return server.getChannelById(channelId);
            }
        }
        for (User user : api.getUsers()) {
            if (channelId.equals(((ImplUser) user).getUserChannelId())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getAuthor().getName() + ": " + getContent() + " (id: " + getId() + ")";
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
