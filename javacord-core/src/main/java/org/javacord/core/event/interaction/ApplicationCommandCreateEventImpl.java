package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.ApplicationCommandCreateEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link ApplicationCommandCreateEvent}.
 */
public class ApplicationCommandCreateEventImpl extends EventImpl implements ApplicationCommandCreateEvent {

    private final Interaction interaction;

    /**
     * Creates a new application command create event.
     *
     * @param interaction The created interaction.
     */
    public ApplicationCommandCreateEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }

}
