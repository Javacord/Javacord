package de.btobastian.javacord.impl;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.Role;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;
import de.btobastian.javacord.api.permissions.Permissions;

/**
 * The implementation of {@link Channel}.
 */
class ImplChannel implements Channel {

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);
    
    private ImplServer server;
    private String id;
    private String name;
    private String topic;
    private int position;
    
    private HashMap<String, Permissions> overriddenPermissions = new HashMap<>();
    
    protected ImplChannel(JSONObject channel, ImplServer server) {
        this.server = server;
        id = channel.getString("id");
        name = channel.getString("name");
        try {
            topic = channel.getString("topic");
        } catch (JSONException e) {
            topic = null;
        }
        position = channel.getInt("position");
        
        JSONArray permissionOverwrites = channel.getJSONArray("permission_overwrites");
        for (int i = 0; i < permissionOverwrites.length(); i++) {
            JSONObject permissionOverrite = permissionOverwrites.getJSONObject(i);
            String id = permissionOverrite.getString("id");
            int allow = permissionOverrite.getInt("allow");
            int deny = permissionOverrite.getInt("deny");
            String type = permissionOverrite.getString("type");
            if (type.equals("role")) {
                for (Role role : server.getRoles()) {
                    if (role.getId().equals(id)) {
                        ((ImplRole) role).setOverriddenPermissions(this, new ImplPermissions(allow, deny));
                    }
                }
            }
            if (type.equals("member")) {
                User user = server.getApi().getUserById(id);
                if (user != null) {
                    overriddenPermissions.put(user.getId(), new ImplPermissions(allow, deny));
                }
            }
        }
        
        server.addChannel(this);
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
        String[] mentionsString = new String[0];
        String json = new JSONObject().put("content", message).put("mentions", mentionsString).put("tts", tts).toString();
        
        String response;
        try {
            response = server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/" + id + "/messages", json, true, "POST");
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
            return null;
        }
        return new ImplMessage(new JSONObject(response), server.getApi(), this);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getTopic()
     */
    @Override
    public String getTopic() {
        return topic;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getPosition()
     */
    @Override
    public int getPosition() {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getServer()
     */
    @Override
    public Server getServer() {
        return server;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#delete()
     */
    @Override
    public boolean delete() {
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/:id" + id, "", true, "DELETE");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        for (Channel channel : getServer().getChannels()) {
            if (channel != this) {
                // inform the other channels
                ((ImplChannel) channel).updatePosition(position, Integer.MAX_VALUE);
            }
        }
        server.removeChannel(this);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#type()
     */
    @Override
    public void type() {
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/" + id + "/typing", "", true, "POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#createInvite()
     */
    @Override
    public String createInvite() {
        String respone;
        try {
            respone = server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/" + id + "/invites", "", true, "POST");
        } catch (IOException e) {
            return null;
        }
        return new JSONObject(respone).getString("code");
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getOverriddenPermissions(de.btobastian.javacord.api.User)
     */
    @Override
    public Permissions getOverriddenPermissions(User user) {
        Permissions permissions = overriddenPermissions.get(user.getId());
        return permissions == null ? emptyPermissions : permissions;
    }
    
    /**
     * Updates the position if an other channel was moved.
     * 
     * @param from The old position of the moved channel.
     * @param to The new postion of the moved channel.
     */
    private void updatePosition(int from, int to) {
        if (from > position && to < position) {
            position++;
        }
        if (from < position && to > position) {
            position--;
        }
    }
}
