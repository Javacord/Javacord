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
package de.btobastian.javacord.utils.handler;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class handles the ready packet.
 */
public class ReadyHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ReadyHandler(DiscordApi api) {
        super(api, false, "READY");
    }

    @Override
    public void handle(JSONObject packet) {
        JSONArray guilds = packet.getJSONArray("guilds");
        for (int i = 0; i < guilds.length(); i++) {
            JSONObject guild = guilds.getJSONObject(i);
            if (guild.has("unavailable") && guild.getBoolean("unavailable")) {
                // add guild to the list of unavailable servers
                // TODO
                continue;
            }
            // TODO create Server object
        }

        JSONArray privateChannels = packet.getJSONArray("private_channels");
        for (int i = 0; i < privateChannels.length(); i++) {
            JSONObject privateChannel = privateChannels.getJSONObject(i);
            String id = privateChannel.getString("id");
            if (privateChannel.has("recipient")) {
                // TODO Create private channel object
            }
        }

        // TODO api.setYourself(api.getOrCreateUser(packet.getJSONObject("user")));
    }

}