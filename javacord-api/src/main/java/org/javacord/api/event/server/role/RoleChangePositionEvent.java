package org.javacord.api.event.server.role;

/**
 * A role change position event.
 */
public interface RoleChangePositionEvent extends RoleEvent {

    /**
     * Gets the new position of the role.
     *
     * @return The new position of the role.
     */
    int getNewPosition();

    /**
     * Gets the old position of the role.
     *
     * @return The old position of the role.
     */
    int getOldPosition();

}
