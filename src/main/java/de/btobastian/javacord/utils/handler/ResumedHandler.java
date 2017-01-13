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

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

/**
 * This class handles the ready packet.
 */
public class ResumedHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ResumedHandler(ImplDiscordAPI api) {
        super(api, false, "RESUMED");
    }

    @Override
    public void handle(JSONObject packet) {
        // Dummy implementation
    }
}
