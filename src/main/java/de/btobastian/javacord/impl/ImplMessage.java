package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.MessageReceiver;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;

class ImplMessage implements Message {

    private ImplDiscordAPI api;
    
    private String id;
    private boolean tts;
    @SuppressWarnings("unused")
    private String timestamp;
    private String content;
    private User author;
    private MessageReceiver receiver;
    
    private final List<User> mentions = new ArrayList<>();
    
    protected ImplMessage(JSONObject message, ImplDiscordAPI api, MessageReceiver receiver) {
        this.api = api;
        this.receiver = receiver;
        
        id = message.getString("id");
        tts = message.getBoolean("tts");
        timestamp = message.getString("timestamp");
        content = message.getString("content");
        
        JSONArray mentionsArray = message.getJSONArray("mentions");
        for (int i = 0; i < mentionsArray.length(); i++) {
            String userId = mentionsArray.getJSONObject(i).getString("id");
            User user = api.getUserById(userId);
            mentions.add(user);
        }
        
        if (receiver == null) {
            String channelId = message.getString("channel_id");
            outer: for (Server server : api.getServers()) {
                for (Channel c : server.getChannels()) {
                    if (c.getId().equals(channelId)) {
                        this.receiver = c;
                        break outer;
                    }
                }
            }
            for (User user : api.getUsers()) {
                if (channelId.equals(((ImplUser) user).getUserChannelId())) {
                    this.receiver = user;
                    break;
                }
            }
        }
        
        author = api.getUserById(message.getJSONObject("author").getString("id"));
        api.addMessage(this);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getContent()
     */
    @Override
    public String getContent() {
        return content;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getChannelReceiver()
     */
    @Override
    public Channel getChannelReceiver() {
        return (Channel) (receiver instanceof Channel ? receiver : null);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getUserReceiver()
     */
    @Override
    public User getUserReceiver() {
        return (User) (receiver instanceof Channel ? receiver : null);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getReceiver()
     */
    @Override
    public MessageReceiver getReceiver() {
        return receiver;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getAuthor()
     */
    @Override
    public User getAuthor() {
        return author;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#isPrivateMessage()
     */
    @Override
    public boolean isPrivateMessage() {
        return getChannelReceiver() == null;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#getMentions()
     */
    @Override
    public List<User> getMentions() {
        return new ArrayList<>(mentions);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#reply(java.lang.String)
     */
    @Override
    public Message reply(String message) {
        return receiver.sendMessage(message);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#reply(java.lang.String, boolean)
     */
    @Override
    public Message reply(String message, boolean tts) {
        return receiver.sendMessage(message, tts);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Message#isTts()
     */
    @Override
    public boolean isTts() {
        return tts;
    }

    protected void update(JSONObject data) {
        try {
            content = data.getString("content");
        } catch (JSONException e) { 
        }
    }
    
    protected ImplDiscordAPI getApi() {
        return api;
    }

}
