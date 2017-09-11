package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A Role event.
 */
public abstract class RoleEvent extends ServerEvent {

    /**
     * The role of the event
     */
    private final Role role;

    /**
     * Creates a new role event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     */
    public RoleEvent(DiscordApi api, Server server, Role role) {
        super(api, server);
        this.role = role;
    }

    /**
     * Gets the role of the event.
     *
     * @return The role of the event.
     */
    public Role getRole() {
        return role;
    }
}
