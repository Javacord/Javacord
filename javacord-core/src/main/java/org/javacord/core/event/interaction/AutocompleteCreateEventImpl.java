package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.AutocompleteCreateEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link AutocompleteCreateEvent}.
 */
public class AutocompleteCreateEventImpl extends EventImpl implements AutocompleteCreateEvent {

    private final Interaction interaction;

    /**
     * Creates a new autocomplete create event.
     *
     * @param interaction The created interaction.
     */
    public AutocompleteCreateEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }

}
