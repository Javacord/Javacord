package org.javacord.event.server.role;

import org.javacord.entity.permission.Role;
import org.javacord.event.server.ServerEvent;

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
