package org.javacord.api.event.interaction;

import org.javacord.api.event.Event;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.MessageComponentInteraction;
import java.util.Optional;

/**
 * A button click event.
 */
public interface ButtonClickEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Gets the created interaction as ButtonInteraction.
     *
     * @return The interaction.
     */
    default ButtonInteraction getButtonInteraction() {
        return getInteraction().asMessageComponentInteraction().get().asButtonInteraction().get();
    }

    /**
     * Gets the created interaction as ButtonInteraction, if the custom id equals the given custom id.
     *
     * @param customId The custom id to match.
     * @return The interaction.
     */
    default Optional<ButtonInteraction> getButtonInteractionWithCustomId(String customId) {
        return getInteraction().asMessageComponentInteractionWithCustomId(customId)
            .flatMap(MessageComponentInteraction::asButtonInteraction);
    }
}
