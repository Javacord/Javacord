package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
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
     * @param role The role of the event.
     * @param oldManaged The old managed flag of the role.
     */
    public RoleChangeManagedEvent(DiscordApi api, Role role, boolean oldManaged) {
        super(api, role);
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
