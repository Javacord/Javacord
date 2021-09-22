package org.javacord.api.event.interaction;

import org.javacord.api.event.Event;
import org.javacord.api.interaction.Interaction;

public interface ApplicationCommandEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();
}
