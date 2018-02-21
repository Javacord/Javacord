package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role change name.
 */
public class RoleChangeNameEvent extends RoleEvent {

    /**
     * The new name of the role.
     */
    private final String newName;

    /**
     * The old name of the role.
     */
    private final String oldName;

    /**
     * Creates a new role change name.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param newName The new name of the role.
     * @param oldName The old name of the role.
     */
    public RoleChangeNameEvent(DiscordApi api, Role role, String newName, String oldName) {
        super(api, role);
        this.newName = newName;
        this.oldName = oldName;
    }

    /**
     * Gets the old name of the role.
     *
     * @return The old name of the role.
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Gets the new name of the role.
     *
     * @return The new name of the role.
     */
    public String getNewName() {
        return newName;
    }
}
