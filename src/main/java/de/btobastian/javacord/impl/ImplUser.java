package de.btobastian.javacord.impl;

import java.io.IOException;

import org.json.JSONObject;

import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.User;

/**
 * The implementation of {@link User}.
 */
class ImplUser implements User {

    private ImplDiscordAPI api;
    
    private String id;
    private String name;
    private String userChannelId;
    
    protected ImplUser(JSONObject user, ImplDiscordAPI api) {
        this.api = api;
        api.addUser(this);
        
        id = user.getString("id");
        name = user.getString("username");
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.MessageReceiver#sendMessage(java.lang.String)
     */
    @Override
    public Message sendMessage(String message) {
        return sendMessage(message, false);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.MessageReceiver#sendMessage(java.lang.String, boolean)
     */
    @Override
    public Message sendMessage(String message, boolean tts) {
        if (userChannelId == null || userChannelId.equals("null")) {
            String response;
            try {
                response = api.getRequestUtils().request("https://discordapp.com/api/users/" + id + "/channels",
                        new JSONObject().put("recipient_id", id).toString(), true, "POST");
            } catch (IOException e) {
                if (api.debug()) {
                    e.printStackTrace();
                }
                return null;
            }
            userChannelId = new JSONObject(response).getString("id");
        }
        
        String[] mentionsString = new String[0];
        String json = new JSONObject().put("content", message).put("mentions", mentionsString).put("tts", tts).toString();
        
        String response;
        try {
            response = api.getRequestUtils().request("https://discordapp.com/api/channels/" + userChannelId + "/messages", json, true, "POST");
        } catch (IOException e) {
            if (api.debug()) {
                e.printStackTrace();
            }
            return null;
        }
        return new ImplMessage(new JSONObject(response), api, null);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.User#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.User#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.User#type()
     */
    @Override
    public void type() {
        if (userChannelId == null) {
            return;
        }
        try {
            api.getRequestUtils().request("https://discordapp.com/api/channels/" + userChannelId + "/typing", "", true, "POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.User#isYourself()
     */
    @Override
    public boolean isYourself() {
        return api.getYourself() == this;
    }
    
    protected void setUserChannelId(String channelId) {
        this.userChannelId = channelId;
    }
    
    protected String getUserChannelId() {
        return userChannelId;
    }
    
    protected ImplDiscordAPI getApi() {
        return api;
    }

}
