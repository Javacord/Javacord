package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionType;

import java.util.List;
import java.util.Optional;

/**
 * This class represents a voice channel.
 */
public interface VoiceChannel extends Channel {

    /**
     * Checks if the given user can connect to the voice channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can connect or not.
     */
    default boolean canConnect(User user) {
        if (!canSee(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
                || severTextChannel.get().hasAnyPermission(user,
                                                           PermissionType.ADMINISTRATOR,
                                                           PermissionType.VOICE_CONNECT);
    }

    /**
     * Checks if the user of the connected account can connect to the voice channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can connect or not.
     */
    default boolean canYouConnect() {
        return canConnect(getApi().getYourself());
    }

    /**
     * Checks if the given user can mute other users in this voice channel.
     * In private chats (private channel or group channel) this always returns <code>false</code>.
     *
     * @param user The user to check.
     * @return Whether the given user can mute other users or not.
     */
    default boolean canMuteUsers(User user) {
        if (!canConnect(user) || getType() == ChannelType.PRIVATE_CHANNEL || getType() == ChannelType.GROUP_CHANNEL) {
            return false;
        }
        Optional<ServerVoiceChannel> serverVoiceChannel = asServerVoiceChannel();
        return !serverVoiceChannel.isPresent()
                || serverVoiceChannel.get().hasAnyPermission(user,
                                                             PermissionType.ADMINISTRATOR,
                                                             PermissionType.VOICE_MUTE_MEMBERS);
    }

    /**
     * Checks if the user of the connected account can mute other users in this voice channel.
     * In private chats (private channel or group channel) this always returns {@code false}.
     *
     * @return Whether the user of the connected account can mute other users or not.
     */
    default boolean canYouMuteUsers() {
        return canMuteUsers(getApi().getYourself());
    }

    /**
     * Gets the list of users connected to this voice-channel.
     *
     * @return The list of users connected to this voice-channel.
     */
    List<User> getConnectedUsers();

    /**
     * Adds a user to this voice-channel's connected-users list.
     *
     * @param user The user to add to this voice-channel's connected-users list.
     */
    default void addConnectedUser(User user) {
        getConnectedUsers().add(user);
    }

    /**
     * Removes a user from this voice-channel's connected-users list.
     *
     * @param user The user to remove from this voice-channel's connected-users list.
     */
    default void removeConnectedUser(User user) {
        getConnectedUsers().remove(user);
    }

}
