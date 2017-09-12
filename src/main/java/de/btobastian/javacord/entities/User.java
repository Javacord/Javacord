package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.listeners.message.MessageCreateListener;

import java.util.List;
import java.util.Optional;

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

}
