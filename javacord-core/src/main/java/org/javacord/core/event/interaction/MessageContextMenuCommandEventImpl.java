package org.javacord.core.event.interaction;

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link MessageContextMenuCommandEvent}.
 */
public class MessageContextMenuCommandEventImpl extends EventImpl implements MessageContextMenuCommandEvent {

    private final Interaction interaction;

    /**
     * Creates a new message context menu command event.
     *
     * @param interaction The created interaction.
     */
    public MessageContextMenuCommandEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }
}
