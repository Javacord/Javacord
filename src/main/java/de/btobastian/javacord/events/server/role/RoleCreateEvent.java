package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.permissions.Role;

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
