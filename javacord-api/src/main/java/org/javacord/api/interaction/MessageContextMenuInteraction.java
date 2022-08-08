package org.javacord.api.interaction;

import org.javacord.api.entity.message.Message;

public interface MessageContextMenuInteraction extends ApplicationCommandInteraction {

    /**
     * Gets the target id.
     *
     * @return The target id.
     */
    long getTargetId();

    /**
     * Gets the target id.
     *
     * @return The target id.
     */
    default String getTargetIdAsString() {
        return String.valueOf(getTargetId());
    }

    /**
     * Gets the target message.
     *
     * @return The target message.
     */
    Message getTarget();
}
