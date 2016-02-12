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
package de.btobastian.javacord;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.utils.ThreadPool;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * This is the most important class of the api.
 *
 * Every instance represents an account.
 * If you want to connect to more than one discord account you have to use more instances.
 */
public interface DiscordAPI {

    /**
     * Connects to the account with the given email and password.
     *
     * This method is non-blocking.
     *
     * @param callback The callback will inform you when the connection is ready.
     *                 The connection is ready as soon as the ready packet was received.
     */
    public void connect(FutureCallback<DiscordAPI> callback);

    /**
     * Connects to the account with the given email and password.
     *
     * This method is blocking! It's recommended to use the non-blocking version which
     * uses a thread from the internal used thread pool to connect.
     */
    public void connectBlocking();

    /**
     * Sets the email address which should be used to connect to the account.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email);

    /**
     * Sets the password which should be used to connect to the account.
     *
     * @param password The password to set.
     */
    public void setPassword(String password);

    /**
     * Sets the game shown in the user list.
     *
     * @param game The game to set.
     */
    public void setGame(String game);

    /**
     * Gets the game shown in the user list.
     *
     * @return The game.
     */
    public String getGame();

    /**
     * Gets a server by its id.
     *
     * @param id The id of the server.
     * @return The server with the given id. <code>Null</code> if no server with the id was found.
     */
    public Server getServerById(String id);

    /**
     * Gets a collection with all known servers.
     *
     * @return A collection with all known servers.
     */
    public Collection<Server> getServers();

    /**
     * Gets an user by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id. <code>Null</code> if no user with the id was found.
     */
    public User getUserById(String id);

    /**
     * Gets a collection with all known users.
     *
     * @return A collection with all known users.
     */
    public Collection<User> getUsers();

    /**
     * Registers a listener.
     *
     * @param listener The listener to register.
     */
    public void registerListener(Listener listener);

    /**
     * Gets a message by its id.
     * This method may return <code>null</code> even if the message exists!
     *
     * @param id The id of the message.
     * @return The message with the given id or <code>null</code> no message was found.
     */
    public Message getMessageById(String id);

    /**
     * Gets the used thread pool of this plugin.
     *
     * The {@link ThreadPool} contains the used thread pool(s) of this api.
     * Don't use multi-threading if you don't known how to made things thread-safe
     * or how to prevent stuff like deadlocks!
     *
     * @return The used thread pool.
     */
    public ThreadPool getThreadPool();

    /**
     * Sets the idle state of the bot.
     *
     * @param idle Whether the bot is idle or not.
     */
    public void setIdle(boolean idle);

    /**
     * Checks if the bot is idle.
     *
     * @return Whether the bot is idle or not.
     */
    public boolean isIdle();

    /**
     * Gets the token of the current connection.
     * It's recommended to store this token somewhere and use it to login instead of always connecting using
     * email and password. Discord will block too many token requests.
     *
     * @return The token of the current connection. <code>Null</code> if the bot isn't connected.
     */
    public String getToken();

    /**
     * Sets the token which is used to connect. You don't need email and password if you have a token, but it's
     * recommended to set email and password, too. The api will try to connect using the token first. If this
     * fails (e.g. if the token is expired) it will use the email and password.
     *
     * @param token The token to set.
     */
    public void setToken(String token);

    /**
     * Checks if the token is valid.
     *
     * @param token The token to check.
     * @return Whether the token is valid or not.
     */
    public boolean checkTokenBlocking(String token);

    /**
     * Accepts an invite.
     *
     * @param inviteCode The invite code.
     * @return The server.
     */
    public Future<Server> acceptInvite(String inviteCode);

    /**
     * Accepts an invite.
     *
     * @param inviteCode The invite code.
     * @param callback The callback which will be informed when you joined the server or joining failed.
     * @return The server.
     */
    public Future<Server> acceptInvite(String inviteCode, FutureCallback<Server> callback);

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @return The created server.
     */
    public Future<Server> createServer(String name);

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @param callback The callback which will be informed when you created the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, FutureCallback<Server> callback);

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @param icon The icon of the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, BufferedImage icon);

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @param icon The icon of the server.
     * @param callback The callback which will be informed when you created the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, BufferedImage icon, FutureCallback<Server> callback);

}
