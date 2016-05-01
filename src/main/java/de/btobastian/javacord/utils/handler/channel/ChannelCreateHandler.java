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
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.entities.impl.ImplChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.impl.ImplVoiceChannel;
import de.btobastian.javacord.listener.channel.ChannelCreateListener;
import de.btobastian.javacord.listener.voicechannel.VoiceChannelCreateListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

/**
 * Handles the channel create packet.
 */
public class ChannelCreateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ChannelCreateHandler.class);

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
            User recipient = api.getOrCreateUser(packet.getJSONObject("recipient"));
            ((ImplUser) recipient).setUserChannelId(packet.getString("id"));
            return;
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
        if (server.getChannelById(packet.getString("id")) != null) {
            return;
        }
        final Channel channel = new ImplChannel(packet, (ImplServer) server, api);
        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<ChannelCreateListener> listeners = api.getListeners(ChannelCreateListener.class);
                synchronized (listeners) {
                    for (ChannelCreateListener listener : listeners) {
                        try {
                            listener.onChannelCreate(api, channel);
                        } catch (Throwable t) {
                            logger.warn("Uncaught exception in ChannelCreateListener!", t);
                        }
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
        if (server.getVoiceChannelById(packet.getString("id")) != null) {
            return;
        }
        final VoiceChannel channel = new ImplVoiceChannel(packet, (ImplServer) server, api);
        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<VoiceChannelCreateListener> listeners = api.getListeners(VoiceChannelCreateListener.class);
                synchronized (listeners) {
                    for (VoiceChannelCreateListener listener : listeners) {
                        try {
                            listener.onVoiceChannelCreate(api, channel);
                        } catch (Throwable t) {
                            logger.warn("Uncaught exception in VoiceChannelCreateListener!", t);
                        }
                    }
                }
            }
        });
    }

}
