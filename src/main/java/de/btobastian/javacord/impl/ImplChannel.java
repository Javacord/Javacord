package de.btobastian.javacord.impl;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.Server;

/**
 * The implementation of {@link Channel}.
 */
class ImplChannel implements Channel {

    private ImplServer server;
    private String id;
    private String name;
    private String topic;
    private int position;
    
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
