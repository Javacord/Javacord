package de.btobastian.javacord.event.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.event.server.ServerEvent;

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
     * @param role The role of the event.
     */
    public RoleEvent(DiscordApi api, Role role) {
        super(api, role.getServer());
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
