package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.entities.message.emoji.Emoji;

/**
 * This class represents a reaction.
 */
public interface Reaction {

    /**
     * Gets the emoji of the reaction.
     *
     * @return The emoji of the reaction.
     */
    Emoji getEmoji();

    /**
     * Gets the amount of users who used this reaction.
     *
     * @return The amount of users who used this reaction.
     */
    int getCount();

    /**
     * Checks if you (the current account) used this reaction.
     *
     * @return Whether this reaction is used by you or not.
     */
    boolean containsYou();

}
