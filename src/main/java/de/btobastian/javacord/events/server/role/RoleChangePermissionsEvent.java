package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role change permissions event.
 */
public class RoleChangePermissionsEvent extends RoleEvent {

    /**
     * The new permissions of the role.
     */
    private final Permissions newPermissions;

    /**
     * The old permissions of the role.
     */
    private final Permissions oldPermissions;

    /**
     * Creates a new role change color event.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param oldPermissions The old permissions of the role.
     * @param newPermissions The new permissions of the role.
     */
    public RoleChangePermissionsEvent(
            DiscordApi api, Role role, Permissions newPermissions, Permissions oldPermissions) {
        super(api, role);
        this.newPermissions = newPermissions;
        this.oldPermissions = oldPermissions;
    }

    /**
     * Gets the new permissions of the role.
     *
     * @return The new permissions of the role.
     */
    public Permissions getNewPermissions() {
        return newPermissions;
    }

    /**
     * Gets the old permissions of the role.
     *
     * @return The old permissions of the role.
     */
    public Permissions getOldPermissions() {
        return oldPermissions;
    }
}
