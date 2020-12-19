package org.javacord.core.event.command;

import org.javacord.api.command.Interaction;
import org.javacord.api.event.command.InteractionCreateEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link InteractionCreateEventImpl}.
 */
public class InteractionCreateEventImpl extends EventImpl implements InteractionCreateEvent {

    private final Interaction interaction;

    /**
     * Creates a new interaction create event.
     *
     * @param interaction The created interaction.
     */
    public InteractionCreateEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }
}

