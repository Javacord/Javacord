package org.javacord.api.event.message.reaction;

import java.util.concurrent.CompletableFuture;

/**
 * A reaction add event.
 */
public interface ReactionAddEvent extends SingleReactionEvent {

    /**
     * Removes the added reaction.
     *
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> removeReaction();

}
