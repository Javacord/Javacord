package org.javacord.api.interaction;

import org.javacord.api.entity.user.User;

public interface UserContextMenuInteraction extends ApplicationCommandInteraction {

    /**
     * Gets the target user.
     *
     * @return The target user.
     */
    User getTarget();
}
