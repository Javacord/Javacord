package de.btobastian.javacord.entities;

import java.util.Optional;

/**
 * This class represents a game as it is displayed in Discord.
 */
public interface Game {

    /**
     * Gets the type of the game.
     *
     * @return The type of the game.
     */
    GameType getType();

    /**
     * Gets the name of the game.
     *
     * @return The name of the game.
     */
    String getName();

    /**
     * Gets the streaming url of the game.
     *
     * @return The streaming url of the game.
     */
    Optional<String> getStreamingUrl();

}
