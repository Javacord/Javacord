package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;

/**
 * The implementation of {@link Server}.
 */
class ImplServer implements Server {

    private final ArrayList<Channel> channels = new ArrayList<>();    
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
    
    protected void addChannel(ImplChannel channel) {
        channels.add(channel);
    }
    
    protected void removeChannel(ImplChannel channel) {
        channels.remove(channel);
    }
    
    protected ImplDiscordAPI getApi() {
        return api;
    }

}
