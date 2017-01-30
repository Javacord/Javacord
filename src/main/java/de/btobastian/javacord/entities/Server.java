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
import de.btobastian.javacord.entities.permissions.Ban;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * This interface represents a discord server (also known as guild).
 */
public interface Server {

    /**
     * Gets the unique id of the server.
     *
     * @return The unique id of the server.
     */
    public String getId();

    /**
     * Gets the name of the server.
     *
     * @return The name of the server.
     */
    public String getName();

    /**
     * Deletes the server.
     *
     * @return A future which tells us if the deletion was successful or not.
     */
    public Future<Void> delete();

    /**
     * Leaves the server.
     *
     * @return A future which tells us if the deletion was successful or not.
     */
    public Future<Void> leave();

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     *         <code>Null</code> if the server has no channel with the given id.
     */
    public Channel getChannelById(String id);

    /**
     * Gets a collection with all channels of the server.
     *
     * @return A collection with all channels of the server.
     */
    public Collection<Channel> getChannels();

    /**
     * Gets a voice channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     *         <code>Null</code> if the server has no channel with the given id.
     */
    public VoiceChannel getVoiceChannelById(String id);

    /**
     * Gets a collection with all voice channels of the server.
     *
     * @return A collection with all voice channels of the server.
     */
    public Collection<VoiceChannel> getVoiceChannels();

    /**
     * Gets an user by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     *         <code>Null</code> if the user is no member of this server.
     */
    public User getMemberById(String id);

    /**
     * Gets a collection with all known members on this server.
     * For large servers some members might not be included in this collection because they are offline.
     *
     * @return A collection with all known members on this server.
     */
    public Collection<User> getMembers();

    /**
     * Checks if an user is a member of this server.
     *
     * @param user The user to check.
     * @return Whether the user is a member or not.
     */
    public boolean isMember(User user);

    /**
     * Checks if an user is a member of this server.
     *
     * @param userId The id of the user to check.
     * @return Whether the user is a member or not.
     */
    public boolean isMember(String userId);

    /**
     * Gets a collection with all roles of this server.
     *
     * @return A collection with all roles of this server.
     */
    public Collection<Role> getRoles();

    /**
     * Gets a role by its id.
     *
     * @param id The id of the role.
     * @return The role with the given id.
     *         <code>Null</code> if the role does not exist on this server.
     */
    public Role getRoleById(String id);

    /**
     * Creates a new channel.
     *
     * @param name The name of the channel.
     * @return The created channel.
     */
    public Future<Channel> createChannel(String name);

    /**
     * Creates a new channel.
     *
     * @param name The name of the channel.
     * @param callback The callback which will be informed when the channel was created.
     * @return The created channel.
     */
    public Future<Channel> createChannel(String name, FutureCallback<Channel> callback);

    /**
     * Creates a new voice channel.
     *
     * @param name The name of the voice channel.
     * @return The created voice channel.
     */
    public Future<VoiceChannel> createVoiceChannel(String name);

    /**
     * Creates a new voice channel.
     *
     * @param name The name of the voice channel.
     * @param callback The callback which will be informed when the channel was created.
     * @return The created voice channel.
     */
    public Future<VoiceChannel> createVoiceChannel(String name, FutureCallback<VoiceChannel> callback);

    /**
     * Gets an array with all invites.
     *
     * @return An array with all invites.
     */
    public Future<Invite[]> getInvites();

    /**
     * Gets an array with all invites.
     *
     * @param callback The callback which will be informed when the request has finished.
     * @return An array with all invites.
     */
    public Future<Invite[]> getInvites(FutureCallback<Invite[]> callback);

    /**
     * Updates the roles of a user.
     *
     * @param user The user.
     * @param roles The roles to set. This will override the existing roles of the user.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> updateRoles(User user, Role[] roles);

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @return A future which tells us whether the ban was successful or not.
     */
    public Future<Void> banUser(User user);

    /**
     * Bans the given user from the server.
     *
     * @param userId The id of the user to ban.
     * @return A future which tells us whether the ban was successful or not.
     */
    public Future<Void> banUser(String userId);

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @param deleteDays Deletes all messages of the user which are younger than <code>deleteDays</code> days.
     * @return A future which tells us whether the ban was successful or not.
     */
    public Future<Void> banUser(User user, int deleteDays);

    /**
     * Bans the given user from the server.
     *
     * @param userId The id of the user to ban.
     * @param deleteDays Deletes all messages of the user which are younger than <code>deleteDays</code> days.
     * @return A future which tells us whether the ban was successful or not.
     */
    public Future<Void> banUser(String userId, int deleteDays);

    /**
     * Unbans the user from the server.
     *
     * @param userId The id of the user to unban.
     * @return A future which tells us whether the unban was successful or not.
     */
    public Future<Void> unbanUser(String userId);

    /**
     * Gets an array with all bans.
     *
     * @return An array with all bans.
     */
    public Future<Ban[]> getBans();

    /**
     * Gets an array with all bans.
     *
     * @param callback The callback which will be informed when the request finished.
     * @return An array with all bans.
     */
    public Future<Ban[]> getBans(FutureCallback<Ban[]> callback);

    /**
     * Kicks the given user from the server.
     *
     * @param user The user to kick.
     * @return A future which tells us whether the kick was successful or not.
     */
    public Future<Void> kickUser(User user);

    /**
     * Kicks the given user from the server.
     *
     * @param userId The id of the user to kick.
     * @return A future which tells us whether the kick was successful or not.
     */
    public Future<Void> kickUser(String userId);

    /**
     * Creates a new role.
     *
     * @return The created role.
     */
    public Future<Role> createRole();

    /**
     * Creates a new role.
     *
     * @param callback The callback which will be informed when the role was created.
     * @return The created role.
     */
    public Future<Role> createRole(FutureCallback<Role> callback);

    /**
     * Updates the name of the server.
     * Use {@link #update(String, Region, BufferedImage)} if you want to change region or/and icon, too
     * or your changes may be overwritten.
     *
     * @param newName The new name of the server.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> updateName(String newName);

    /**
     * Updates the region of the server.
     * Use {@link #update(String, Region, BufferedImage)} if you want to change the name, too
     * or your changes may be overwritten.
     *
     * @param newRegion The new region of the server.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> updateRegion(Region newRegion);

    /**
     * Updates the icon of the server.
     * Use {@link #update(String, Region, BufferedImage)} if you want to change the name, too
     * or your changes may be overwritten.
     *
     * @param newIcon The new icon of the server.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> updateIcon(BufferedImage newIcon);

    /**
     * Updates the server.
     *
     * @param newName The name of the server. Set it to <code>null</code> if you don't want to change the name.
     * @param newRegion The region of the server. Set it to <code>null</code> if you don't want to change the region.
     * @param newIcon The icon of the server. Set it to <code>null</code> if you don't want to change the icon.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> update(String newName, Region newRegion, BufferedImage newIcon);

    /**
     * Gets the region of the server.
     *
     * @return The region of the server.
     */
    public Region getRegion();

    /**
     * Gets the amount of members on this server.
     *
     * @return The amount of members on this server.
     */
    public int getMemberCount();

    /**
     * Checks whether the server is large or not.
     * A large server has more than 250 members. Discord don't send offline users for large servers so
     * {@link #getMemberCount()} might be greater than the size of {@link #getMembers()}.
     *
     * @return Whether the server is large or not.
     */
    public boolean isLarge();

    /**
     * Gets the id of the server owner.
     *
     * @return The id of the server owner.
     */
    public String getOwnerId();

    /**
     * Gets the owner of the server.
     *
     * @return A future which contains the owner of the server.
     */
    public Future<User> getOwner();

    /**
     * Authorizes a bot to join the server.
     *
     * @param applicationId The id of the bot's application.
     * @return A future which tells us whether the authorization was successful or not.
     */
    public Future<Void> authorizeBot(String applicationId);

    /**
     * Authorizes a bot to join the server.
     *
     * @param applicationId The id of the bot's application.
     * @param permissions The permissions the bot should get on join.
     * @return A future which tells us whether the authorization was successful or not.
     */
    public Future<Void> authorizeBot(String applicationId, Permissions permissions);

    /**
     * Gets a collection with all custom emojis on this server.
     *
     * @return A collection with all custom emojis on this server.
     */
    public Collection<CustomEmoji> getCustomEmojis();

    /**
     * Gets a custom emoji by its id.
     *
     * @param id The id of the emoji.
     * @return The emoji with the given id.
     *         <code>Null</code> if the emoji does not exist on this server.
     */
    public CustomEmoji getCustomEmojiById(String id);

    /**
     * Gets a custom emoji by its name.
     *
     * @param name The name of the emoji.
     * @return The emoji with the given name.
     *         <code>Null</code> if the emoji does not exist on this server.
     */
    public CustomEmoji getCustomEmojiByName(String name);

    /**
     * Gets the nickname of the user on the server.
     *
     * @param user The user.
     * @return The nickname of the user on the server or <code>null</code> if the user has no nick.
     */
    public String getNickname(User user);

    /**
     * Checks if the user has a nickname on the server.
     *
     * @param user The user to check.
     * @return Whether the user has a nickname on the server or not.
     */
    public boolean hasNickname(User user);

    /**
     * Updates the nickname of a user.
     *
     * @param user The user to modify.
     * @param nickname The nickname to set.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> updateNickname(User user, String nickname);

}
