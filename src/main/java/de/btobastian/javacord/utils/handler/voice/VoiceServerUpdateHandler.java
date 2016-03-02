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
package de.btobastian.javacord.utils.handler.voice;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

/**
 * Handles the voice server update packet.
 */
public class VoiceServerUpdateHandler extends PacketHandler {

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
        // String serverId = packet.getString("guild_id");
        String token = packet.getString("token");
        String endpoint = packet.getString("endpoint");
        api.getSocket().setVoiceTokenAndEndpoint(token, endpoint.startsWith("wss://") ? endpoint : "wss://" + endpoint);
    }

}
