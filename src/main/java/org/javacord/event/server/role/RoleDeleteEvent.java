package org.javacord.event.server.role;

import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;

/**
 * A role delete event.
 */
public class RoleDeleteEvent extends RoleEvent {

    /**
     * Creates a new role delete event.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     */
    public RoleDeleteEvent(DiscordApi api, Role role) {
        super(api, role);
    }

}
