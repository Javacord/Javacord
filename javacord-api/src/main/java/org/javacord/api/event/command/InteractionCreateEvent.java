package org.javacord.api.event.command;

import org.javacord.api.command.Interaction;
import org.javacord.api.entity.message.InteractionMessageBuilder;
import org.javacord.api.event.Event;

import java.util.concurrent.CompletableFuture;

/**
 * An interaction create event.
 */
public interface InteractionCreateEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Send a response to a slash command. Must be executed withing 3 seconds or the token will be invalidated.
     * If it may take longer to respond, use {@link #respondLater()}.
     *
     * @return The InteractionMessageBuilder to respond to the interaction.
     */
    InteractionMessageBuilder respond();

    /**
     * Send a "loading state" as your first response. You can edit and send followup messages withing 15 minutes.
     *
     * @return A Completable future when the "loading state" response has been sent.
     */
    CompletableFuture<Void> respondLater();

    /**
     * Deletes the initial response.
     *
     * @return The completable future when the message has been deleted.
     */
    CompletableFuture<Void> deleteInitialResponse();

}
