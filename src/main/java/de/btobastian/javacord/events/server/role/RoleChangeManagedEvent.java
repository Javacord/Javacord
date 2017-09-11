package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role change managed event.
 */
public class RoleChangeManagedEvent extends RoleEvent {

    /**
     * The old managed value.
     */
    private final boolean oldManaged;

    /**
     * Creates a new role change managed event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param oldManaged The old managed flag of the role.
     */
    public RoleChangeManagedEvent(DiscordApi api, Server server, Role role, boolean oldManaged) {
        super(api, server, role);
        this.oldManaged = oldManaged;
    }

    /**
     * Gets the old managed flag of the role.
     *
     * @return The old managed flag of the role.
     */
    public boolean getOldManagedFlag() {
        return oldManaged;
    }

    /**
     * Gets the new managed flag of the role.
     *
     * @return The new managed flag of the role.
     */
    public boolean getNewManagedFlag() {
        return !oldManaged;
    }

}
