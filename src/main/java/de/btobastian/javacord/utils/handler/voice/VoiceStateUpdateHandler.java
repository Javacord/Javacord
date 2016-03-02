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
 * Handles the voice state update packet.
 */
public class VoiceStateUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceStateUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "VOICE_STATE_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        String userId = packet.getString("user_id");
        String sessionId = packet.getString("session_id");
        String serverId = packet.getString("guild_id");
        String channelId = null;
        if (!packet.isNull("channel:id")) {
            packet.getString("channel_id");
        }
        boolean selfMute = packet.getBoolean("self_mute");
        boolean mute = packet.getBoolean("mute");
        boolean selfDeaf = packet.getBoolean("self_deaf");
        boolean deaf = packet.getBoolean("deaf");
        boolean suppress = packet.getBoolean("suppress");

        if (userId.equals(api.getYourself().getId())) {
            api.getSocket().setVoiceSessionId(sessionId);
        }
    }

}
