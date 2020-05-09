package org.javacord.api.event.server.role;

/**
 * A role change position event.
 */
public interface RoleChangePositionEvent extends RoleEvent {

    /**
     * Gets the new real position of the role.
     *
     * @return The new real position of the role.
     */
    int getNewPosition();

    /**
     * Gets the old real position of the role.
     *
     * @return The old real position of the role.
     */
    int getOldPosition();

    /**
     * Gets the new raw position of the role.
     *
     * @return The new raw position of the role.
     */
    int getNewRawPosition();

    /**
     * Gets the old raw position of the role.
     *
     * @return The old raw position of the role.
     */
    int getOldRawPosition();

}
