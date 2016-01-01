package de.btobastian.javacord.impl;

import java.io.IOException;

import org.json.JSONObject;

import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.VoiceChannel;

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
        for (VoiceChannel channel : getServer().getVoiceChannels()) {
            if (channel != this) {
                // inform the other channels
                ((ImplVoiceChannel) channel).updatePosition(position, Integer.MAX_VALUE);
            }
        }
        server.removeVoiceChannel(this);
        return true;
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
