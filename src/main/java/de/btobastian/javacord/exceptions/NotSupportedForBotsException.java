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
package de.btobastian.javacord.exceptions;

/**
 * This exception is always thrown when you try to access a method which
 * bots are not able to use (e.g. joining an invite).
 */
public class NotSupportedForBotsException extends IllegalStateException {

    /**
     * Creates a new instance of this class.
     */
    public NotSupportedForBotsException() {
        super("Bots are not able to use this method!");
    }


    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     */
    public NotSupportedForBotsException(String message) {
        super(message);
    }

}
