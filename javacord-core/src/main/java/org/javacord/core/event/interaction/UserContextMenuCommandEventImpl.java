package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.UserContextMenuCommandEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link UserContextMenuCommandEvent}.
 */
public class UserContextMenuCommandEventImpl extends EventImpl implements UserContextMenuCommandEvent {

    private final Interaction interaction;

    /**
     * Creates a new user context menu command event.
     *
     * @param interaction The created interaction.
     */
    public UserContextMenuCommandEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }
}
