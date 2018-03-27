package org.javacord.api.event.server.role;

/**
 * A role change hoist event.
 */
public interface RoleChangeHoistEvent extends RoleEvent {

    /**
     * Gets the old hoist of the role.
     *
     * @return The old hoist of the role.
     */
    boolean getOldHoist();

    /**
     * Gets the new hoist of the role.
     *
     * @return The new hoist of the role.
     */
    boolean getNewHoist();

}
