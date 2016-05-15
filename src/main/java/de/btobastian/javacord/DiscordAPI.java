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
import de.btobastian.javacord.entities.*;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.ratelimits.RateLimitManager;

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
     * Sets the game shown in the user list and a streaming url (which means it shows you as streaming).
     *
     * @param game The game to set.
     * @param streamingUrl The url of the stream.
     */
    public void setGame(String game, String streamingUrl);

    /**
     * Gets the game shown in the user list.
     *
     * @return The game.
     */
    public String getGame();

    /**
     * Gets the streaming url of the bot.
     *
     * @return The streaming url of the bot.
     */
    public String getStreamingUrl();

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
     * Gets a collection with all known channels.
     *
     * @return A collection with all known channels.
     */
    public Collection<Channel> getChannels();

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id. <code>Null</code> if no channel with the id was found.
     */
    public Channel getChannelById(String id);

    /**
     * Gets a collection with all known voice channels.
     *
     * @return A collection with all known voice channels.
     */
    public Collection<VoiceChannel> getVoiceChannels();

    /**
     * Gets a voice channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id. <code>Null</code> if no channel with the id was found.
     */
    public VoiceChannel getVoiceChannelById(String id);

    /**
     * Gets an user by its id. It first will check if the user is in the cache. If no user was found in the cache it
     * tries to request it from the api.
     *
     * @param id The id of the user.
     * @return The user with the given id. <code>Null</code> if no user with the id was found.
     */
    public Future<User> getUserById(String id);

    /**
     * Gets an user by its id. Unlike {@link #getUserById(String)} this method only search for the user in the cache.
     * Some members of bigger servers may not be in the cache cause discord only sends the online users for servers
     * with more than 250 members.
     *
     * @param id The id of the user.
     * @return The user with the given id. <code>Null</code> if no user with the id is in the cache.
     */
    public User getCachedUserById(String id);

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
     * @param bot Whether the token is the token of a bot account or a normal account.
     */
    public void setToken(String token, boolean bot);

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
     * @param region The region of the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, Region region);

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @param region The region of the server.
     * @param callback The callback which will be informed when you created the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, Region region, FutureCallback<Server> callback);

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

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @param region The region of the server.
     * @param icon The icon of the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, Region region, BufferedImage icon);

    /**
     * Creates a new server.
     *
     * @param name The name of the new server.
     * @param region The region of the server.
     * @param icon The icon of the server.
     * @param callback The callback which will be informed when you created the server.
     * @return The created server.
     */
    public Future<Server> createServer(String name, Region region, BufferedImage icon, FutureCallback<Server> callback);

    /**
     * Gets yourself (the user with which you logged in).
     *
     * @return Yourself.
     */
    public User getYourself();

    /**
     * Updates the username.
     * If you want to update the email or password, too, use
     * {@link #updateProfile(String, String, String, BufferedImage)}.
     * Otherwise the first update will be overridden (except you wait for it to finish using {@link Future#get()}).
     *
     * @param newUsername The new username.
     * @return A future which tells us whether the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updateUsername(String newUsername);

    /**
     * Updates the email address.
     * Attention: Do not mix up with {@link #setEmail(String)}! This method changes the account settings!
     * If you want to update the username or password, too, use
     * {@link #updateProfile(String, String, String, BufferedImage)}.
     * Otherwise the first update will be overridden (except you wait for it to finish using {@link Future#get()}).
     *
     * @param newEmail The new email.
     * @return A future which tells us whether the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updateEmail(String newEmail);

    /**
     * Updates the password.
     * Attention: Do not mix up with {@link #setPassword(String)}! This method changes the account settings!
     * If you want to update the username or email, too, use
     * {@link #updateProfile(String, String, String, BufferedImage)}.
     * Otherwise the first update will be overridden (except you wait for it to finish using {@link Future#get()}).
     *
     * @param newPassword The new password.
     * @return A future which tells us whether the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updatePassword(String newPassword);

    /**
     * Updates the avatar.
     * If you want to update the username, password or email, too, use
     * {@link #updateProfile(String, String, String, BufferedImage)}.
     * Otherwise the first update will be overridden (except you wait for it to finish using {@link Future#get()}).
     *
     * @param newAvatar The new avatar.
     * @return A future which tells us whether the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updateAvatar(BufferedImage newAvatar);

    /**
     * Updates the profile.
     *
     * @param newUsername The new username. Set to <code>null</code> to keep the current name.
     * @param newEmail The new email. Set to <code>null</code> to keep the current email.
     * @param newPassword The new password. Set to <code>null</code> to keep the current password.
     * @param newAvatar The new avatar. Set to <code>null</code> to keep the current avatar.
     * @return A future which tells us whether the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updateProfile(
            String newUsername, String newEmail, String newPassword, BufferedImage newAvatar);

    /**
     * Tries to parse the given invite.
     *
     * @param invite The invite code or the invite url.
     * @return The parsed invite.
     */
    public Future<Invite> parseInvite(String invite);

    /**
     * Tries to parse the given invite.
     *
     * @param invite The invite code or the invite url.
     * @param callback The callback which will be informed when the invite has been parsed.
     * @return The parsed invite.
     */
    public Future<Invite> parseInvite(String invite, FutureCallback<Invite> callback);

    /**
     * Deletes the invite with the given code.
     *
     * @param inviteCode The invite code.
     * @return A future which tells us whether the deletion was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> deleteInvite(String inviteCode);

    /**
     * Sets the size of message cache.
     * If the cache is full the oldest message in the cache will be removed.
     *
     * @param size The size of the cache.
     */
    public void setMessageCacheSize(int size);

    /**
     * Gets the size of the message cache.
     * If the cache is full the oldest message in the cache will be removed.
     *
     * @return The size of the cache.
     */
    public int getMessageCacheSize();

    /**
     * Tries to close the current connection and connect again.
     *
     * @param callback The callback which will be informed when we receive the ready packet (again).
     */
    public void reconnect(FutureCallback<DiscordAPI> callback);

    /**
     * Tries to close the current connection and connect again.
     */
    public void reconnectBlocking();

    /**
     * Sets whether the api should try to auto-reconnect or not.
     *
     * @param autoReconnect Whether the api should try to auto-reconnect or not.
     */
    public void setAutoReconnect(boolean autoReconnect);

    /**
     * Gets whether the api should try to auto-reconnect or not.
     *
     * @return Whether the api should try to auto-reconnect or not.
     */
    public boolean isAutoReconnectEnabled();

    /**
     * Gets a collection with all applications you own.
     *
     * @return A collection with all applications you own.
     */
    public Future<Collection<Application>> getApplications();

    /**
     * Gets a collection with all applications you own.
     *
     * @param callback The callback which will be informed when the request finished.
     * @return A collection with all applications you own.
     */
    public Future<Collection<Application>> getApplications(FutureCallback<Collection<Application>> callback);

    /**
     * Create a new application.
     *
     * @param name The name of the application.
     * @return The created application.
     */
    public Future<Application> createApplication(String name);

    /**
     * Create a new application.
     *
     * @param name The name of the application.
     * @param callback The callback which will be informed when the application was created.
     * @return The created application.
     */
    public Future<Application> createApplication(String name, FutureCallback<Application> callback);

    /**
     * Gets an application by its id.
     *
     * @param id The id of the application.
     * @return The application with the given id.
     */
    public Future<Application> getApplication(String id);

    /**
     * Gets an application by its id.
     *
     * @param id The id of the application.
     * @param callback The callback which will be informed when the request finished.
     * @return The application with the given id.
     */
    public Future<Application> getApplication(String id, FutureCallback<Application> callback);

    /**
     * Deletes an application.
     *
     * @param id The id of the application.
     * @return A future which tells us whether the deletion was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> deleteApplication(String id);

    /**
     * Creates a new bot and an application for this bot.
     *
     * @param name The name of the bot and application.
     * @return The application which owns the bot.
     */
    public Future<Application> createBot(String name);

    /**
     * Creates a new bot and an application for this bot.
     *
     * @param name The name of the bot and application.
     * @param callback The callback which will be informed when the bot was created.
     * @return The application which owns the bot.
     */
    public Future<Application> createBot(String name, FutureCallback<Application> callback);

    /**
     * Creates a new bot for an application.
     *
     * @param name The name of the bot.
     * @param applicationId The id of the application.
     * @return The application which owns the bot.
     */
    public Future<Application> createBot(String name, String applicationId);

    /**
     * Creates a new bot for an application.
     *
     * @param name The name of the bot.
     * @param applicationId The id of the application.
     * @param callback The callback which will be informed when the bot was created.
     * @return The application which owns the bot.
     */
    public Future<Application> createBot(String name, String applicationId, FutureCallback<Application> callback);

    /**
     * Gets the rate limit manager. This class caches all rate limits of the api.
     *
     * @return The rate limit manager of the api.
     */
    public RateLimitManager getRateLimitManager();

    /**
     * Sets whether the bot should wait for all servers to be loaded or not.
     *
     * This value is <code>true</code> by default.
     * If it's set to <code>false</code> the list of servers ({@link #getServers()}) will be empty after connecting and
     * will be filled a few seconds later (depending on the amount of servers).
     *
     * @param wait Whether the bot should wait for all servers to be loaded or not.
     */
    public void setWaitForServersOnStartup(boolean wait);

    /**
     * Checks whether the bot should wait for all servers to be loaded or not.
     *
     * This value is <code>true</code> by default.
     * If it's set to <code>false</code> the list of servers ({@link #getServers()}) will be empty after connecting and
     * will be filled a few seconds later (depending on the amount of servers).
     */
    public boolean isWaitingForServersOnStartup();

    /**
     * Disconnects the bot.
     * After disconnecting you should NOT use this instance again.
     */
    public void disconnect();

}
