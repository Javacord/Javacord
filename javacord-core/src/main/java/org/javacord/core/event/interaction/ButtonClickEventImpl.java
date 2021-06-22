package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link ButtonClickEvent}.
 */
public class ButtonClickEventImpl extends EventImpl implements ButtonClickEvent {

    private final Interaction interaction;

    /**
     * Creates a new button click event.
     *
     * @param interaction The created interaction.
     */
    public ButtonClickEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }
}
