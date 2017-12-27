package de.btobastian.javacord.listeners.message.reaction;

import de.btobastian.javacord.events.message.reaction.ReactionRemoveAllEvent;

/**
 * This listener listens to all reaction being delete at once.
 */
@FunctionalInterface
public interface ReactionRemoveAllListener {

    /**
     * This method is called every time all reactions were removed from a message.
     *
     * @param event The event.
     */
    void onReactionRemoveAll(ReactionRemoveAllEvent event);

}
