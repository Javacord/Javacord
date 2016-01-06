package de.btobastian.javacord.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.InviteBuilder;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.ChannelChangePositionListener;
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener;
import de.btobastian.javacord.message.Message;
import de.btobastian.javacord.permissions.Permissions;
import de.btobastian.javacord.permissions.Role;

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
            topic = "";
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
     * @see de.btobastian.javacord.api.Channel#getOverriddenPermissions(de.btobastian.javacord.api.User)
     */
    @Override
    public Permissions getOverriddenPermissions(User user) {
        Permissions permissions = overriddenPermissions.get(user.getId());
        return permissions == null ? emptyPermissions : permissions;
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.Channel#getInviteBuilder()
     */
    @Override
    public InviteBuilder getInviteBuilder() {
        return new ImplInviteBuilder(this); 
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.Channel#updateOverriddenPermissions(de.btobastian.javacord.User, de.btobastian.javacord.permissions.Permissions)
     */
    @Override
    public boolean updateOverriddenPermissions(User user, Permissions permissions) {
        JSONObject jsonParam = new JSONObject()
            .put("allow", ((ImplPermissions) permissions).getAllow())
            .put("deny", ((ImplPermissions) permissions).getDeny())
            .put("type", "member")
            .put("id", user.getId());
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/" + id + "/permissions/" + user.getId(), jsonParam.toString(), true, "PUT");
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
            return false;
        }
        overriddenPermissions.put(user.getId(), permissions);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.Channel#updateName(java.lang.String)
     */
    @Override
    public boolean updateName(String name) {
        return update(name, position, topic);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.Channel#updatePosition(int)
     */
    @Override
    public boolean updatePosition(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("The position cannot be less than 0!");
        }
        if (position > server.getChannels().size() - 1) {
            throw new IllegalArgumentException("The position cannot be greater than the amount of channels - 1!");
        }
        return update(name, position, topic);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.Channel#updateTopic(java.lang.String)
     */
    @Override
    public boolean updateTopic(String topic) {
        return update(name, position, topic);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.message.MessageReceiver#sendFile(java.io.File)
     */
    @Override
    public Message sendFile(File file) {
        try {
            MultipartUtility multipart = new MultipartUtility("https://discordapp.com/api/channels/" + id + "/messages", "UTF-8", server.getApi());
            multipart.addFilePart("file", file);
            return new ImplMessage(new JSONObject(multipart.finish().get(0)), server.getApi(), this);
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    protected void setOverriddenPermissions(User user, Permissions permissions) {
        overriddenPermissions.put(user.getId(), permissions);
    }
    
    protected void setName(String name) {
        this.name = name;
    }
    
    protected void setPosition(int position) {
        this.position = position;
    }
    
    protected void setTopic(String topic) {
        this.topic = topic;
    }
    
    private boolean update(String name, int position, String topic) {
        JSONObject jsonParam = new JSONObject()
            .put("name", name)
            .put("position", position)
            .put("topic", topic);
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/" + id, jsonParam.toString(), true, "PATCH");
        } catch (IOException e) {
            if (server.getApi().debug()) {
                e.printStackTrace();
            }
            return false;
        }
        if (!this.name.equals(name)) {
            String oldName = this.name;
            this.name = name;
            for (Listener listener : server.getApi().getListeners(ChannelChangeNameListener.class)) {
                ((ChannelChangeNameListener) listener).onChannelChangeName(server.getApi(), this, oldName);
            }
        }
        if (this.position != position) {
            int oldPosition = this.position;
            this.position = position;
            for (Listener listener : server.getApi().getListeners(ChannelChangePositionListener.class)) {
                ((ChannelChangePositionListener) listener).onChannelChangePosition(server.getApi(), this, oldPosition);
            }
        }
        if (!this.topic.equals(topic)) {
            String oldTopic = this.topic;
            this.topic = topic;
            for (Listener listener : server.getApi().getListeners(ChannelChangeTopicListener.class)) {
                ((ChannelChangeTopicListener) listener).onChannelChangeTopic(server.getApi(), this, oldTopic);
            }
        }
        return true;
    }
}
