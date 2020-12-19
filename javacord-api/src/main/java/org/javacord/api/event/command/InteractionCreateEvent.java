package org.javacord.api.event.command;

import org.javacord.api.command.Interaction;
import org.javacord.api.event.Event;

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
}
