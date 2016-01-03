package de.btobastian.javacord.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.User;
import de.btobastian.javacord.message.Message;

/**
 * The implementation of {@link User}.
 */
class ImplUser implements User {

    private ImplDiscordAPI api;
    
    private String id;
    private String name;
    private String userChannelId;
    private String avatarId;
    
    protected ImplUser(JSONObject user, ImplDiscordAPI api) {
        this.api = api;
        
        id = user.getString("id");
        name = user.getString("username");
        try {
            avatarId = user.getString("avatar");
        } catch (JSONException e) {
            avatarId = null;
        }
        api.addUser(this);
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
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.User#getAvatar()
     */
    @Override
    public byte[] getAvatar() {
        if (avatarId == null) {
            return null;
        }
        try {
            URL url = new URL("https://discordapp.com/api/users/" + id + "/avatars/" + avatarId + ".jpg");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("User-Agent", "Javacord");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
               out.write(buf, 0, n);
            }
            out.close();
            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            if (api.debug()) {
                e.printStackTrace();
            }
            return new byte[0];
        }
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
    
    protected void setName(String name) {
        this.name = name;
    }

}
