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

import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.Future;

/**
 * This interface represents an invite.
 *
 * Note: Unlike the other classes there may be more invite instances for one invite.
 *       Invites won't be updated (e.g. uses).
 */
public interface Invite {

    /**
     * Gets the invite code.
     *
     * @return The invite code.
     */
    public String getCode();

    /**
     * Gets the url of the invite.
     *
     * @return The url of the invite.
     */
    public URL getInviteUrl();

    /**
     * Gets the server id of the invite.
     *
     * @return The server id of the invite.
     */
    public String getServerId();

    /**
     * Gets the server name of the invite.
     *
     * @return The server name of the invite.
     */
    public String getServerName();

    /**
     * Gets the server of the invite. May be <code>null</code>!
     *
     * @return Get's the server of the invite. <code>Null</code> if you are not member of the invite's server.
     */
    public Server getServer();

    /**
     * Gets the channel id of the invite.
     *
     * @return The channel id of the invite.
     */
    public String getChannelId();

    /**
     * Gets the channel name of the invite.
     *
     * @return The channel name of the invite.
     */
    public String getChannelName();

    /**
     * Gets the channel of the invite. May be <code>null</code>!
     *
     * @return Get's the channel of the invite.
     * <code>Null</code> if you are not member of the invite's server or it's a voice channel.
     */
    public Channel getChannel();

    /**
     * Gets the voice channel of the invite. May be <code>null</code>!
     *
     * @return Get's the voice channel of the invite.
     * <code>Null</code> if you are not member of the invite's server or it's a text channel.
     */
    public VoiceChannel getVoiceChannel();

    /**
     * Checks whether the channel is a voice or text channel.
     *
     * @return Whether the channel is a voice channel or not.
     */
    public boolean isVoiceChannel();

    /**
     * Gets the maximum age of the invite.
     *
     * @return The maximum age of the invite. <code>-1</code> if the invite has no max age
     *         or you are not allowed to get this information.
     */
    public int getMaxAge();

    /**
     * Checks if the invite is revoked.
     *
     * @return Whether the invite is revoked or not. May be also <code>true</code> if
     * you are not allowed to get this information.
     */
    public boolean isRevoked();

    /**
     * Gets the date of creation. May be <code>null</code>.
     *
     * @return The date of creation. May be <code>null</code>if you are not allowed to get this information.
     */
    public Calendar getCreationDate();

    /**
     * Gets the uses of the invite.
     *
     * @return The uses of the invite. <code>-1</code> if you are not allowed to get this information.
     */
    public int getUses();

    /**
     * Gets the maximum uses of the invite.
     *
     * @return The maximum uses of the invite. <code>-1</code> if the invite has no limit
     *         or you are not allowed to get this information.
     */
    public int getMaxUses();

    /**
     * Checks if the invite is temporary.
     *
     * @return Whether the invite is temporary or not. May be also <code>false</code> if
     * you are not allowed to get this information.
     */
    public boolean isTemporary();

    /**
     * Gets the creator of the invite.
     *
     * @return The creator of the invite. <code>Null</code> if you are not allowed to get this information.
     */
    public User getCreator();

    /**
     * Accepts the invite.
     *
     * @return The server.
     * @see de.btobastian.javacord.DiscordAPI#acceptInvite(String)
     */
    public Future<Server> acceptInvite();

    /**
     * Accepts the invite.
     *
     * @param callback The callback which will be informed when you joined the server or joining failed.
     * @return The server.
     * @see de.btobastian.javacord.DiscordAPI#acceptInvite(String, FutureCallback)
     */
    public Future<Server> acceptInvite(FutureCallback<Server> callback);

    /**
     * Deletes the invite.
     *
     * @return A future which tells us whether the deletion was successful or not.
     */
    public Future<Void> delete();

}
