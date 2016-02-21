/*
 * Copyright (C) 2016 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.utils.handler.channel;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelDeleteListener;
import de.btobastian.javacord.listener.voicechannel.VoiceChannelDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.List;

/**
 * Handles the channel delete packet.
 */
public class ChannelDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelDeleteHandler(ImplDiscordAPI api) {
        super(api, true, "CHANNEL_DELETE");
    }

    @Override
    public void handle(JSONObject packet) {
        boolean isPrivate = packet.getBoolean("is_private");
        if (isPrivate) {
            return; // TODO ignored atm
        }
        Server server = api.getServerById(packet.getString("guild_id"));
        if (packet.getString("type").equals("text")) {
            handleServerTextChannel(packet, server);
        } else {
            handleServerVoiceChannel(packet, server);
        }
    }

    /**
     * Handles the server text channels.
     *
     * @param packet The packet (the "d"-object).
     * @param server The server of the channel.
     */
    private void handleServerTextChannel(JSONObject packet, Server server) {
        final Channel channel = server.getChannelById(packet.getString("id"));
        ((ImplServer) server).removeChannel(channel);
        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<Listener> listeners =  api.getListeners(ChannelDeleteListener.class);
                synchronized (listeners) {
                    for (Listener listener : listeners) {
                        ((ChannelDeleteListener) listener).onChannelDelete(api, channel);
                    }
                }
            }
        });
    }

    /**
     * Handles the server voice channels.
     *
     * @param packet The packet (the "d"-object).
     * @param server The server of the channel.
     */
    private void handleServerVoiceChannel(JSONObject packet, Server server) {
        final VoiceChannel channel = server.getVoiceChannelById(packet.getString("id"));
        ((ImplServer) server).removeVoiceChannel(channel);
        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<Listener> listeners =  api.getListeners(VoiceChannelDeleteListener.class);
                synchronized (listeners) {
                    for (Listener listener : listeners) {
                        ((VoiceChannelDeleteListener) listener).onVoiceChannelDelete(api, channel);
                    }
                }
            }
        });
    }

}
