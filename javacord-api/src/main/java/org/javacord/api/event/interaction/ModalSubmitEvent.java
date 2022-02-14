package org.javacord.api.event.interaction;

import org.javacord.api.interaction.ModalInteraction;

/**
 * A modal submit event.
 */
public interface ModalSubmitEvent extends ApplicationCommandEvent {

    /**
     * Gets the created interaction as ModalInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default ModalInteraction getModalInteraction() {
        return getInteraction().asModalInteraction().get();
    }

}
