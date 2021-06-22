package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link MessageComponentCreateEvent}.
 */
public class MessageComponentCreateEventImpl extends EventImpl implements MessageComponentCreateEvent {

    private final Interaction interaction;

    /**
     * Creates a new interaction create event.
     *
     * @param interaction The created interaction.
     */
    public MessageComponentCreateEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }

}
