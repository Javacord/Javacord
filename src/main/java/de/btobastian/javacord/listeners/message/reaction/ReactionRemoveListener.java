package de.btobastian.javacord.listeners.message.reaction;

import de.btobastian.javacord.events.message.reaction.ReactionRemoveEvent;

/**
 * This listener listens to reaction deletions.
 */
@FunctionalInterface
public interface ReactionRemoveListener {

    /**
     * This method is called every time a reaction is removed from a message.
     *
     * @param event The event.
     */
    void onReactionRemove(ReactionRemoveEvent event);

}
