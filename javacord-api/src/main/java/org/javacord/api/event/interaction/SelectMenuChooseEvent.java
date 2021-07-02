package org.javacord.api.event.interaction;

import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SelectMenuInteraction;

import java.util.Optional;

/**
 * A select menu choose event.
 */
public interface SelectMenuChooseEvent {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Gets the created interaction as SelectMenuInteraction.
     *
     * @return The interaction.
     */
    default SelectMenuInteraction getSelectMenuInteraction() {
        return getInteraction().asMessageComponentInteraction().get().asSelectMenuInteraction().get();
    }

    /**
     * Gets the created interaction as SelectMenuInteraction, if the custom id equals the given custom id.
     *
     * @param customId The custom id to match.
     * @return The interaction.
     */
    default Optional<SelectMenuInteraction> getSelectMenuInteractionWithCustomId(String customId) {
        return getInteraction().asMessageComponentInteractionWithCustomId(customId)
                .flatMap(MessageComponentInteraction::asSelectMenuInteraction);
    }

}
