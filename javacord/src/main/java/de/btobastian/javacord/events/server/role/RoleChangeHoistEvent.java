package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role change hoist event.
 */
public class RoleChangeHoistEvent extends RoleEvent {

    /**
     * The old hoist of the role.
     * (Whether it is pinned separately in the user list or not)
     */
    private final boolean oldHoist;

    /**
     * Creates a new role change hoist event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param oldHoist The old hoist of the role.
     */
    public RoleChangeHoistEvent(DiscordApi api, Server server, Role role, boolean oldHoist) {
        super(api, server, role);
        this.oldHoist = oldHoist;
    }

    /**
     * Gets the old hoist of the role.
     *
     * @return The old hoist of the role.
     */
    public boolean getOldHoist() {
        return oldHoist;
    }

    /**
     * Gets the new hoist of the role.
     *
     * @return The new hoist of the role.
     */
    public boolean getNewHoist() {
        return !oldHoist;
    }
}
