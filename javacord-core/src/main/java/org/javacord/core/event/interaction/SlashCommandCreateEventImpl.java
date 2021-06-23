package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link SlashCommandCreateEvent}.
 */
public class SlashCommandCreateEventImpl extends EventImpl implements SlashCommandCreateEvent {

    private final Interaction interaction;

    /**
     * Creates a new slash command create event.
     *
     * @param interaction The created interaction.
     */
    public SlashCommandCreateEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }

}
