package org.javacord.api.event.interaction;

import org.javacord.api.event.Event;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.SlashCommandInteraction;
import java.util.Optional;

/**
 * An slash command create event.
 */
public interface SlashCommandCreateEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Gets the created interaction as SlashCommandInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default SlashCommandInteraction getSlashCommandInteraction() {
        return getInteraction().asSlashCommandInteraction().get();
    }

    /**
     * Gets the created interaction as SlashCommandInteraction, if the interaction is of this type and the
     * command id equals the given command id.
     *
     * @param commandId The command it to match.
     * @return The interaction.
     */
    default Optional<SlashCommandInteraction> getSlashCommandInteractionWithCommandId(long commandId) {
        return getInteraction().asSlashCommandInteractionWithCommandId(commandId);
    }

}
