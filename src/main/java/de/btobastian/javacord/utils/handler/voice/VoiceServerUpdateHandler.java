/*
 * Copyright (C) 2017 Bastian Oppermann
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
package de.btobastian.javacord.utils.handler.voice;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import de.btobastian.javacord.utils.audio.ImplAudioManager;
import org.json.JSONObject;
import org.slf4j.Logger;

/**
 * Handles the voice state update packet.
 */
public class VoiceServerUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(VoiceServerUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceServerUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "VOICE_SERVER_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        String server = packet.getString("guild_id");
        String gateway = packet.getString("endpoint").replace(":80", "");
        String token = packet.getString("token");
    
        Server srv = api.getServerById(server);
        if (server == null) return;
    
        ImplAudioManager manager = (ImplAudioManager) srv.getAudioManager();
        if (manager.isConnected()) manager.regionChange();
        if (!manager.isConnecting()) {
            logger.debug("Received " + getType() + " while not trying to connect to channel.");
            return;
        }
        
        api.getAudioObjFactory().voiceServerUpdate(server, gateway, token);
    }

}
