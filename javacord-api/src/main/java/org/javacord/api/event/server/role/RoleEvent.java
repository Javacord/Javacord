package org.javacord.api.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.ServerEvent;

/**
 * A Role event.
 */
public interface RoleEvent extends ServerEvent {

    /**
     * Gets the role of the event.
     *
     * @return The role of the event.
     */
    Role getRole();

}
