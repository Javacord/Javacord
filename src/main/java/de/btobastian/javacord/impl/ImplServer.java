package de.btobastian.javacord.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;
import de.btobastian.javacord.api.VoiceChannel;

/**
 * The implementation of {@link Server}.
 */
class ImplServer implements Server {

    private final ArrayList<Channel> channels = new ArrayList<>();
    private final ArrayList<VoiceChannel> voicechannels = new ArrayList<>();      
    private final ArrayList<User> users = new ArrayList<>();
    
    private String name;
    private String id;
    private User owner;
    private ImplDiscordAPI api;
    
    /**
     * Class constructor.
     * 
     * @param guild The {@link JSONObject} containing all information.
     * @param api The api.
     */
    protected ImplServer(JSONObject guild, ImplDiscordAPI api) {
        this.api = api;
        name = guild.getString("name");
        id = guild.getString("id");
        String ownerId = guild.getString("owner_id");
        
        JSONArray channels = guild.getJSONArray("channels");        
        for (int i = 0; i < channels.length(); i++) {
            JSONObject channel = channels.getJSONObject(i);
            String type = channel.getString("type");
            if (type.equals("text")) { // a text channel
                new ImplChannel(channel, this);
            }
            if (type.equals("voice")) { // a voice channel
                new ImplVoiceChannel(channel, this);
            }
        }

        JSONArray members = guild.getJSONArray("members");
        for (int i = 0; i < members.length(); i++) {
            JSONObject member = members.getJSONObject(i);
            JSONObject userJson = member.getJSONObject("user");
            User user = api.getUser(userJson);
            users.add(user);
            if (user.getId().equals(ownerId)) {
                owner = user;
            }
        }
        
        api.addServer(this);
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getOwner()
     */
    @Override
    public User getOwner() {
        return owner;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getChannels()
     */
    @Override
    public List<Channel> getChannels() {
        return new ArrayList<Channel>(channels);
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#createChannel(java.lang.String)
     */
    @Override
    public Channel createChannel(String name) {
        Object channel = createChannel(name, false);
        if (channel != null) {
            return (Channel) channel;
        } else {
            return null;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#createVoiceChannel(java.lang.String)
     */
    @Override
    public VoiceChannel createVoiceChannel(String name) {
        Object channel = createChannel(name, true);
        if (channel != null) {
            return (VoiceChannel) channel;
        } else {
            return null;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#deleteOrLeave()
     */
    @Override
    public void deleteOrLeave() {
        try {
            api.getRequestUtils().request("https://discordapp.com/api/guilds/" + id, "", true, "DELETE");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        getApi().removeServer(this);
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#kick(de.btobastian.javacord.api.User)
     */
    public boolean kick(User user) {
        try {
            getApi().getRequestUtils().request("https://discordapp.com/api/guilds/" + id + "/members/" + user.getId(), "", true, "DELETE");
        } catch (IOException e) {
            if (getApi().debug()) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getVoiceChannels()
     */
    @Override
    public List<VoiceChannel> getVoiceChannels() {
        return new ArrayList<>(voicechannels);
    }
    
    private Object createChannel(String name, boolean voice) {
        String json = new JSONObject().put("name", name).put("type", voice ? "voice" : "text").toString();
        
        String response;
        try {
            response = getApi().getRequestUtils().request("https://discordapp.com/api/guilds/" + id + "/channels", json, true, "POST");
        } catch (IOException e) {
            if (getApi().debug()) {
                e.printStackTrace();
            }
            return null;
        }
        if (voice) {
            return new ImplVoiceChannel(new JSONObject(response), this);
        } else {
            return new ImplChannel(new JSONObject(response), this);
        }
    }
    
    protected void addChannel(ImplChannel channel) {
        channels.add(channel);
    }
    
    protected void removeChannel(ImplChannel channel) {
        channels.remove(channel);
    }
    
    protected void addVoiceChannel(ImplVoiceChannel channel) {
        voicechannels.add(channel);
    }
    
    protected void removeVoiceChannel(ImplVoiceChannel channel) {
        voicechannels.remove(channel);
    }
    
    protected ImplDiscordAPI getApi() {
        return api;
    }

}
