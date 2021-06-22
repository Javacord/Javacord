package org.javacord.api.event.interaction;

import org.javacord.api.event.Event;
import org.javacord.api.interaction.ApplicationCommandInteraction;
import org.javacord.api.interaction.Interaction;
import java.util.Optional;

/**
 * An application command create event.
 */
public interface ApplicationCommandCreateEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Gets the created interaction as ApplicationCommandInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default ApplicationCommandInteraction getApplicationCommandInteraction() {
        return getInteraction().asApplicationCommandInteraction().get();
    }

    /**
     * Gets the created interaction as ApplicationCommandInteraction, if the interaction is of this type and the
     * command id equals the given command id.
     *
     * @param commandId The command it to match.
     * @return The interaction.
     */
    default Optional<ApplicationCommandInteraction> getApplicationCommandInteractionWithCommandId(long commandId) {
        return getInteraction().asApplicationCommandInteractionWithCommandId(commandId);
    }

}
