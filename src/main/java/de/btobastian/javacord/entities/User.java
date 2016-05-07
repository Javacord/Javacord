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

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.permissions.Role;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * This interface represents an user.
 */
public interface User extends MessageReceiver {

    /**
     * Gets the id of the user.
     *
     * @return The id of the user.
     */
    public String getId();

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName();

    /**
     * Checks if the user is the account you logged in.
     *
     * @return Whether the user is you or not.
     */
    public boolean isYourself();

    /**
     * Gets the avatar of the user as byte array.
     *
     * @return The jpg-avatar of the user. The array is empty if the user has no avatar.
     */
    public Future<byte[]> getAvatarAsByteArray();

    /**
     * Gets the avatar of the user as byte array.
     *
     * @param callback The callback which will be informed when the avatar was downloaded.
     *                 The array is the jpg-avatar of the user. The array is empty if the user has no avatar.
     * @return The jpg-avatar of the user. The array is empty if the user has no avatar.
     */
    public Future<byte[]> getAvatarAsByteArray(FutureCallback<byte[]> callback);

    /**
     * Gets the avatar of the user.
     *
     * @return The jpg-avatar of the user. Canceled if the user has no avatar.
     */
    public Future<BufferedImage> getAvatar();

    /**
     * Gets the avatar of the user.
     *
     * @param callback The callback which will be informed when the avatar was downloaded.
     *                 The image will be <code>null</code> if the user has no avatar.
     * @return The jpg-avatar of the user. Canceled if the user has no avatar.
     */
    public Future<BufferedImage> getAvatar(FutureCallback<BufferedImage> callback);

    /**
     * Gets the url of the users avatar.
     *
     * @return The url of the users avatar. <code>Null</code> if the user has no avatar.
     */
    public URL getAvatarUrl();

    /**
     * Gets the id of the users avatar.
     *
     * @return The id of the users avatar. <code>Null</code> if the user has no avatar.
     */
    public String getAvatarId();

    /**
     * Gets the roles of the user on the given server.
     *
     * @param server The server.
     * @return The roles of the user on the given server.
     */
    public Collection<Role> getRoles(Server server);

    /**
     * Gets the game the user is currently playing.
     *
     * @return The game the user is currently playing. May be <code>null</code>.
     */
    public String getGame();

    /**
     * Gets the tag which is used to mention the user.
     *
     * @return Gets the tag which is used to mention the user.
     */
    public String getMentionTag();

    /**
     * Gets the discriminator of the user.
     *
     * @return The discriminator of the user.
     */
    public String getDiscriminator();

    /**
     * Checks whether the user is a bot or not.
     *
     * @return Whether the user is a bot or not.
     */
    public boolean isBot();

    /**
     * Gets the status of the user.
     *
     * @return The status of the user.
     */
    public UserStatus getStatus();

}
