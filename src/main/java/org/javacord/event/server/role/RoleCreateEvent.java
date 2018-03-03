package org.javacord.event.server.role;

import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;

/**
 * A role create event.
 */
public class RoleCreateEvent extends RoleEvent {

    /**
     * Creates a new role create event
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     */
    public RoleCreateEvent(DiscordApi api, Role role) {
        super(api, role);
    }

}
