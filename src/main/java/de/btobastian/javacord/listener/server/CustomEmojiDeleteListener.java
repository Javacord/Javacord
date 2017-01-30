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
package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.listener.Listener;

/**
 * This listener listens to emoji deletions.
 */
public interface CustomEmojiDeleteListener extends Listener {

    /**
     * This method is called every time a emoji was deleted.
     *
     * @param api The api.
     * @param emoji The deleted emoji.
     */
    public void onCustomEmojiDelete(DiscordAPI api, CustomEmoji emoji);

}
