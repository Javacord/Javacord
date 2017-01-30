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

import java.util.concurrent.Future;

/**
 * This interfaces helps to create invites.
 */
public interface InviteBuilder {

    /**
     * Sets the max uses of the invite.
     *
     * @param maxUses The max uses of the invite.
     * @return This object.
     */
    public InviteBuilder setMaxUses(int maxUses);

    /**
     * Sets if the invite is temporary.
     *
     * @param temporary Whether the invite should be temporary or not.
     * @return This object.
     */
    public InviteBuilder setTemporary(boolean temporary);

    /**
     * Sets the max age of the invite.
     *
     * @param maxAge The max age of the invite in seconds.
     * @return This object.
     */
    public InviteBuilder setMaxAge(int maxAge);

    /**
     * Creates the invite.
     *
     * @return The invite.
     */
    public Future<Invite> create();

    /**
     * Creates the invite.
     *
     * @param callback The callback which will be informed when the invite was created or creation failed.
     * @return The invite.
     */
    public Future<Invite> create(FutureCallback<Invite> callback);

}
