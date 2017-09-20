package de.btobastian.javacord.listeners.message.reaction;

import de.btobastian.javacord.events.message.reaction.ReactionAddEvent;

/**
 * This listener listens to reaction adding.
 */
@FunctionalInterface
public interface ReactionAddListener {

    /**
     * This method is called every time a reaction is added to a message.
     *
     * @param event The event.
     */
    void onReactionAdd(ReactionAddEvent event);

}
