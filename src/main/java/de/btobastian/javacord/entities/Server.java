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
 * This interface represents a discord server (also known as guild).
 */
public interface Server {

    /**
     * Gets the name of the server.
     *
     * @return The name of the server.
     */
    public String getName();

    /**
     * Gets the unique id of the server.
     *
     * @return The unique id of the server.
     */
    public String getId();

    /**
     * Deletes the server.
     *
     * @return A future which tells us if the deletion was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> delete();

}
