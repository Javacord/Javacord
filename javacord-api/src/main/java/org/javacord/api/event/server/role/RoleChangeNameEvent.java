package org.javacord.api.event.server.role;

/**
 * A role change name.
 */
public interface RoleChangeNameEvent extends RoleEvent {

    /**
     * Gets the old name of the role.
     *
     * @return The old name of the role.
     */
    String getOldName();

    /**
     * Gets the new name of the role.
     *
     * @return The new name of the role.
     */
    String getNewName();

}
