package org.javacord.api.event.interaction;

import org.javacord.api.event.Event;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.MessageComponentInteraction;
import java.util.Optional;

/**
 * A message component interaction create event.
 */
public interface MessageComponentCreateEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Gets the created interaction as MessageComponentInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default MessageComponentInteraction getMessageComponentInteraction() {
        return getInteraction().asMessageComponentInteraction().get();
    }

    /**
     * Gets the created interaction as MessageComponentInteraction, if the interaction is of this type, and the
     * custom id equals the given custom id.
     *
     * @param customId The custom id to match.
     * @return The interaction.
     */
    default Optional<MessageComponentInteraction> getMessageComponentInteractionWithCustomId(String customId) {
        return getInteraction().asMessageComponentInteractionWithCustomId(customId);
    }

}
