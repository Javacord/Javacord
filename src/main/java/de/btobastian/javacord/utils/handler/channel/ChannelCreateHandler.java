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
import de.btobastian.javacord.entities.impl.ImplChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelCreateListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.List;

/**
 * Handles the channel create packet.
 */
public class ChannelCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelCreateHandler(ImplDiscordAPI api) {
        super(api, true, "CHANNEL_CREATE");
    }

    @Override
    public void handle(JSONObject packet) {
        boolean isPrivate = packet.getBoolean("is_private");
        if (isPrivate) {
            return; // TODO ignored atm
        }
        handleServerChannel(packet);
    }

    /**
     * Handles the server text channels.
     *
     * @param packet The packet (the "d"-object).
     */
    private void handleServerChannel(JSONObject packet) {
        Server server = api.getServerById(packet.getString("guild_id"));
        Channel channel = new ImplChannel(packet, (ImplServer) server, api);

        List<Listener> listeners =  api.getListeners(ChannelCreateListener.class);
        synchronized (listeners) {
            for (Listener listener : listeners) {
                ((ChannelCreateListener) listener).onChannelCreate(api, channel);
            }
        }
    }

}
