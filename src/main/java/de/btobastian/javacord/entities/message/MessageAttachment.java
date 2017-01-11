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
package de.btobastian.javacord.entities.message;

import java.net.URL;

/**
 * This interface represents a message attachment.
 */
public interface MessageAttachment {

    /**
     * Gets the url of the attachment.
     *
     * @return The url of the attachment.
     */
    public URL getUrl();

    /**
     * Gets the proxy url of the attachment.
     *
     * @return The proxy url of the attachment.
     */
    public URL getProxyUrl();

    /**
     * Gets the size of the attachment in bytes.
     *
     * @return The size of the attachment in bytes.
     */
    public int getSize();

    /**
     * Gets the id of the attachment.
     *
     * @return The id of the attachment.
     */
    public String getId();

    /**
     * Gets the file name of the attachment.
     *
     * @return The file name of the attachment.
     */
    public String getFileName();

}
