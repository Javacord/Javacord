package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link ModalSubmitEvent}.
 */
public class ModalSubmitEventImpl extends EventImpl implements ModalSubmitEvent {

    private final Interaction interaction;

    /**
     * Creates a new modal submit event.
     *
     * @param interaction The created interaction.
     */
    public ModalSubmitEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }

}
