package de.btobastian.javacord.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Role;
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
    private final ArrayList<Role> roles = new ArrayList<>();
    
    private String name;
    private String id;
    private User owner;
    private String region;
    
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
        region = guild.getString("region");
        String ownerId = guild.getString("owner_id");
        
        JSONArray roles = guild.getJSONArray("roles");
        for (int i = 0; i < roles.length(); i++) {
            JSONObject role = roles.getJSONObject(i);
            new ImplRole(role, this);
        }
        
        JSONArray channels;
        try {
            channels = guild.getJSONArray("channels");
        } catch (JSONException e) {
            channels = new JSONArray();
        }
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

        JSONArray members;
        try {
            members = guild.getJSONArray("members");
        } catch (JSONException e) {
            members = new JSONArray();
        }
        for (int i = 0; i < members.length(); i++) {
            JSONObject member = members.getJSONObject(i);
            JSONObject userJson = member.getJSONObject("user");
            User user = api.getUser(userJson);
            users.add(user);
            if (user.getId().equals(ownerId)) {
                owner = user;
            }
            
            JSONArray memberRoles = member.getJSONArray("roles");
            for (int j = 0; j < memberRoles.length(); j++) {
                String id = memberRoles.getString(j);
                for (Role role : this.roles) {
                    if (role.getId().equals(id)) {
                        ((ImplRole) role).addUser(user);
                    }
                }
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

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getRoles()
     */
    @Override
    public List<Role> getRoles() {
        return new ArrayList<>(roles);
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#getRegion()
     */
    @Override
    public String getRegion() {
        return region;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#unban(java.lang.String)
     */
    @Override
    public void unban(String userId) {
        try {
            getApi().getRequestUtils().request("https://discordapp.com/api/guilds/" + id + "/bans/" + userId, "", true, "DELETE");
        } catch (IOException e) {
            if (getApi().debug()) {
                e.printStackTrace();
            }
            return;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#ban(de.btobastian.javacord.api.User)
     */
    @Override
    public void ban(User user) {
        ban(user, 0);
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Server#ban(de.btobastian.javacord.api.User, int)
     */
    @Override
    public void ban(User user, int deleteDays) {
        try {
            getApi().getRequestUtils().request(
                    "https://discordapp.com/api/guilds/:guild_id/bans/" + user.getId() + "?delete-message-days=" + deleteDays, "", true, "PUT");
        } catch (IOException e) {
            if (getApi().debug()) {
                e.printStackTrace();
            }
            return;
        }
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
    
    protected void addRole(ImplRole role) {
        roles.add(role);
    }
    
    protected void removeRole(ImplRole role) {
        roles.remove(role);
    }
    
    protected void addUser(User user) {
        users.add(user);
    }
    
    protected void removeUser(User user) {
        users.remove(user);
    }
    
    protected ImplDiscordAPI getApi() {
        return api;
    }

}
