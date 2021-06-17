package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.InteractionCreateEvent;
import org.javacord.api.interaction.ApplicationCommandInteraction;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.core.event.EventImpl;

import java.util.Optional;

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

    @Override
    public Optional<ApplicationCommandInteraction> getApplicationCommandInteraction() {
        return interaction.asApplicationCommandInteraction();
    }

    @Override
    public Optional<MessageComponentInteraction> getMessageComponentInteraction() {
        return interaction.asMessageComponentInteraction();
    }
}
