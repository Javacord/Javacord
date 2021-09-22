package org.javacord.api.event.interaction;

import org.javacord.api.interaction.MessageContextMenuInteraction;
import java.util.Optional;

/**
 * A message context menu command event.
 */
public interface MessageContextMenuCommandEvent extends ApplicationCommandEvent {

    /**
     * Gets the created interaction as MessageContextMenuInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default MessageContextMenuInteraction getMessageContextMenuInteraction() {
        return getInteraction().asMessageContextMenuInteraction().get();
    }

    /**
     * Gets the created interaction as MessageContextMenuInteraction, if the interaction is of this type and the
     * command id equals the given command id.
     *
     * @param commandId The command it to match.
     * @return The interaction.
     */
    default Optional<MessageContextMenuInteraction> getMessageContextMenuInteractionWithCommandId(long commandId) {
        return getInteraction().asMessageContextMenuInteractionWithCommandId(commandId);
    }
}
