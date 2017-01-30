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
package de.btobastian.javacord.entities;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.permissions.Role;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * This interface represents a custom emoji.
 */
public interface CustomEmoji {

    /**
     * Gets the id of the emoji.
     *
     * @return The id of the emoji.
     */
    public String getId();

    /**
     * Gets the name of the emoji.
     *
     * @return The name of the emoji.
     */
    public String getName();

    /**
     * Gets the server of the emoji.
     *
     * @return The server of the emoji.
     */
    public Server getServer();

    /**
     * Gets whether the emoji is managed.
     *
     * @return Whether the emoji is managed.
     */
    public boolean isManaged();

    /**
     * Gets whether the emoji must be wrapped in colons.
     *
     * @return Whether the emoji must be wrapped in colons.
     */
    public boolean requiresColons();

    /**
     * Gets the roles this emoji is active for.
     *
     * @return The roles this emoji is active for.
     */
    public Collection<Role> getRoles();

    /**
     * Gets the tag which is used to display the emoji.
     *
     * @return The tag which is used to display the emoji.
     */
    public String getMentionTag();

    /**
     * Gets the emoji as byte array.
     *
     * @return The png-image of the emoji.
     */
    public Future<byte[]> getEmojiAsByteArray();

    /**
     * Gets the emoji as byte array.
     *
     * @param callback The callback which will be informed when the emoji was downloaded.
     *                 The array is the png-image of the emoji.
     * @return The png-image of the emoji.
     */
    public Future<byte[]> getEmojiAsByteArray(FutureCallback<byte[]> callback);

    /**
     * Gets the emoji.
     *
     * @return The png-image of the emoji.
     */
    public Future<BufferedImage> getEmoji();

    /**
     * Gets the emoji.
     *
     * @param callback The callback which will be informed when the emoji was downloaded.
     * @return The png-image of the emoji.
     */
    public Future<BufferedImage> getEmoji(FutureCallback<BufferedImage> callback);

    /**
     * Gets the url of the emoji image.
     *
     * @return The url of the emoji image.
     */
    public URL getImageUrl();

    /**
     * Deletes the emoji.
     *
     * @return A future which tells us whether the deletion was successful or not.
     */
    public Future<Void> delete();

}
