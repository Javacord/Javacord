package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class represents a Discord server, sometimes also called guild.
 */
public interface Server extends DiscordEntity {

    /**
     * Gets the name of the server.
     *
     * @return The name of the server.
     */
    String getName();

    /**
     * Gets the region of the server.
     *
     * @return The region of the server.
     */
    Region getRegion();

    /**
     * Gets the nickname of a user.
     *
     * @param user The user to check.
     * @return The nickname of the user.
     */
    Optional<String> getNickname(User user);

    /**
     * Gets a collection with all members of the server.
     *
     * @return A collection with all members of the server.
     */
    Collection<User> getMembers();

    /**
     * Checks if the server if considered large.
     *
     * @return Whether the server is large or not.
     */
    boolean isLarge();

    /**
     * Gets the amount of members in this server.
     *
     * @return The amount of members in this server.
     */
    int getMemberCount();

    /**
     * Gets the owner of the server.
     *
     * @return The owner of the server.
     */
    User getOwner();

    /**
     * Gets a collection with all channels of the server.
     *
     * @return A collection with all channels of the server.
     */
    Collection<ServerChannel> getChannels();

    /**
     * Gets a collection with all text channels of the server.
     *
     * @return A collection with all text channels of the server.
     */
    default Collection<ServerTextChannel> getTextChannels() {
        return getChannels().stream()
                .filter(channel -> channel instanceof ServerTextChannel)
                .map(channel -> (ServerTextChannel) channel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets a collection with all voice channels of the server.
     *
     * @return A collection with all voice channels of the server.
     */
    default Collection<ServerVoiceChannel> getVoiceChannels() {
        return getChannels().stream()
                .filter(channel -> channel instanceof ServerVoiceChannel)
                .map(channel -> (ServerVoiceChannel) channel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    Optional<ServerChannel> getChannelById(long id);

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<ServerChannel> getChannelById(String id) {
        try {
            return getChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<ServerTextChannel> getTextChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ServerTextChannel)
                .map(channel -> (ServerTextChannel) channel);
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<ServerTextChannel> getTextChannelById(String id) {
        try {
            return getTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getVoiceChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ServerVoiceChannel)
                .map(channel -> (ServerVoiceChannel) channel);
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getVoiceChannelById(String id) {
        try {
            return getVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Adds a listener, which listens to message creates in this server.
     *
     * @param listener The listener to add.
     */
    void addMessageCreateListener(MessageCreateListener listener);

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    List<MessageCreateListener> getMessageCreateListeners();

    /**
     * Adds a listener, which listens to you leaving this server.
     *
     * @param listener The listener to add.
     */
    void addServerLeaveListener(ServerLeaveListener listener);

    /**
     * Gets a list with all registered server leaves listeners.
     *
     * @return A list with all registered server leaves listeners.
     */
    List<ServerLeaveListener> getServerLeaveListeners();

    /**
     * Adds a listener, which listens to this server becoming unavailable.
     *
     * @param listener The listener to add.
     */
    void addServerBecomesUnavailableListener(ServerBecomesUnavailableListener listener);

    /**
     * Gets a list with all registered server becomes unavailable listeners.
     *
     * @return A list with all registered server becomes unavailable listeners.
     */
    List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners();

    /**
     * Adds a listener, which listens to users starting to type in this server.
     *
     * @param listener The listener to add.
     */
    void addUserStartTypingListener(UserStartTypingListener listener);

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    List<UserStartTypingListener> getUserStartTypingListeners();

    /**
     * Adds a listener, which listens to server channel creations in this server.
     *
     * @param listener The listener to add.
     */
    void addServerChannelCreateListener(ServerChannelCreateListener listener);

    /**
     * Gets a list with all registered server channel create listeners.
     *
     * @return A list with all registered server channel create listeners.
     */
    List<ServerChannelCreateListener> getServerChannelCreateListeners();

    /**
     * Adds a listener, which listens to server channel deletions in this server.
     *
     * @param listener The listener to add.
     */
    void addServerChannelDeleteListener(ServerChannelDeleteListener listener);

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    List<ServerChannelDeleteListener> getServerChannelDeleteListeners();

}
