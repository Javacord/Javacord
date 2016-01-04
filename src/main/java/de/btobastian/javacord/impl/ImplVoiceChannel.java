package de.btobastian.javacord.impl;

import java.io.IOException;

import org.json.JSONObject;

import de.btobastian.javacord.Server;
import de.btobastian.javacord.VoiceChannel;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.voice.VoiceChannelChangeNameListener;
import de.btobastian.javacord.listener.voice.VoiceChannelChangePositionListener;

/**
 * The implementation of {@link VoiceChannel}.
 */
class ImplVoiceChannel implements VoiceChannel {
    
    private ImplServer server;
    private String id;
    private String name;
    private int position;
    
    protected ImplVoiceChannel(JSONObject channel, ImplServer server) {
        this.server = server;
        id = channel.getString("id");
        name = channel.getString("name");
        position = channel.getInt("position");
        
        server.addVoiceChannel(this);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.VoiceChannel#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.VoiceChannel#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.VoiceChannel#getPosition()
     */
    @Override
    public int getPosition() {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.VoiceChannel#getServer()
     */
    @Override
    public Server getServer() {
        return server;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.VoiceChannel#delete()
     */
    @Override
    public boolean delete() {
        try {
            server.getApi().getRequestUtils().request("https://discordapp.com/api/channels/:id" + id, "", true, "DELETE");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        server.removeVoiceChannel(this);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.VoiceChannel#updateName(java.lang.String)
     */
    @Override
    public boolean updateName(String name) {
        return update(name, position);
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.VoiceChannel#updatePosition(int)
     */
    @Override
    public boolean updatePosition(int position) {
        return update(name, position);
    }
    
    protected void setName(String name) {
        this.name = name;
    }
    
    protected void setPosition(int position) {
        this.position = position;
    }
    
    private boolean update(String name, int position) {
        JSONObject jsonParam = new JSONObject()
            .put("name", name)
            .put("position", position);
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
            for (Listener listener : server.getApi().getListeners(VoiceChannelChangeNameListener.class)) {
                ((VoiceChannelChangeNameListener) listener).onVoiceChannelChangeName(server.getApi(), this, oldName);
            }
        }
        if (this.position != position) {
            int oldPosition = this.position;
            this.position = position;
            for (Listener listener : server.getApi().getListeners(VoiceChannelChangePositionListener.class)) {
                ((VoiceChannelChangePositionListener) listener).onVoiceChannelChangePosition(server.getApi(), this, oldPosition);
            }
        }
        return true;
    }

}
