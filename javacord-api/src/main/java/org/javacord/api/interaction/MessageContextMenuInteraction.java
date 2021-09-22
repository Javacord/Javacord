package org.javacord.api.interaction;

import org.javacord.api.entity.message.Message;

public interface MessageContextMenuInteraction extends ApplicationCommandInteraction {

    /**
     * Gets the target message.
     *
     * @return The target message.
     */
    Message getTarget();
}
