package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role create event.
 */
public class RoleCreateEvent extends RoleEvent {

    /**
     * Creates a new role create event
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     */
    public RoleCreateEvent(DiscordApi api, Server server, Role role) {
        super(api, server, role);
    }

}
