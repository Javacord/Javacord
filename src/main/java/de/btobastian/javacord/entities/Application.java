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
package de.btobastian.javacord.entities;

import java.util.concurrent.Future;

/**
 * This interface represents an application.
 *
 * Note: Unlike the other classes there may be more application instances for one application.
 *       Applications won't be updated (e.g. description).
 */
public interface Application {

    /**
     * Gets the id of the application.
     *
     * @return The id of the application.
     */
    public String getId();

    /**
     * Gets the description of the application.
     *
     * @return The description of the application.
     */
    public String getDescription();

    /**
     * Gets the redirect uris of the application.
     *
     * @return The redirect uris of the application.
     */
    public String[] getRedirectUris();

    /**
     * Gets the name of the application.
     *
     * @return The name of the application.
     */
    public String getName();

    /**
     * Gets the secret of the application.
     *
     * @return The secret of the application.
     */
    public String getSecret();

    /**
     * Gets the token of the bot. Returns <code>null</code> if the application has no bot.
     *
     * @return The token of the bot. May be <code>null</code>.
     */
    public String getBotToken();

    /**
     * Gets the bot user. Returns <code>null</code> if the application has no bot.
     *
     * @return The bot user. May be <code>null</code>.
     */
    public User getBot();

    /**
     * Deletes the application.
     *
     * @return A future which tells us whether the deletion was successful or not.
     * @see de.btobastian.javacord.DiscordAPI#deleteApplication(String)
     */
    public Future<Void> delete();

}
