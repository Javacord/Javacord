package org.javacord.api.event.interaction;

import org.javacord.api.interaction.UserContextMenuInteraction;
import java.util.Optional;

/**
 * A user context menu command event.
 */
public interface UserContextMenuCommandEvent extends ApplicationCommandEvent {

    /**
     * Gets the created interaction as UserContextMenuInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default UserContextMenuInteraction getUserContextMenuInteraction() {
        return getInteraction().asUserContextMenuInteraction().get();
    }

    /**
     * Gets the created interaction as UserContextMenuInteraction, if the interaction is of this type and the
     * command id equals the given command id.
     *
     * @param commandId The command it to match.
     * @return The interaction.
     */
    default Optional<UserContextMenuInteraction> getUserContextMenuInteractionWithCommandId(long commandId) {
        return getInteraction().asUserContextMenuInteractionWithCommandId(commandId);
    }
}
