package de.btobastian.javacord.entities;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a user.
 */
public interface User extends DiscordEntity, Messageable, Mentionable {

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    String getName();

    /**
     * Gets the nickname of the user in the given server.
     *
     * @param server The server to check.
     * @return The nickname of the user.
     */
    default Optional<String> getNickname(Server server) {
        return server.getNickname(this);
    }

    /**
     * Gets the private channel with the user.
     * This will only be present, if there was an conversation with the user in the past or you manually opened a
     * private channel with the given user, using {@link #openPrivateChannel()}.
     *
     * @return The private channel with the user.
     */
    Optional<PrivateChannel> getPrivateChannel();

    /**
     * Opens a new private channel with the given user.
     * If there's already a private channel with the user, it will just return the one which already exists.
     *
     * @return The new (or old) private channel with the user.
     */
    CompletableFuture<PrivateChannel> openPrivateChannel();

    /**
     * Gets Whether this user is the user connected to the Discord API.
     * Calls {@link DiscordApi#getYourself()} to get the connected user.
     *
     * @return Whether the user is the user representing the account connected to the Discord API.
     */
    boolean isYourself();

    /**
     * Adds a listener, which listens to message creates from this user.
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
     * Adds a listener, which listens to this user starting to type.
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

}
