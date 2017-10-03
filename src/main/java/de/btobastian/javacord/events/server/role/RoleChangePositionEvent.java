package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role change position event.
 */
public class RoleChangePositionEvent extends RoleEvent {

    /**
     * The new position of the role.
     */
    private final int newPosition;

    /**
     * The old position of the role.
     */
    private final int oldPosition;

    /**
     * Creates a new role change color event.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param oldPosition The old position of the role.
     * @param newPosition The new position of the role.
     */
    public RoleChangePositionEvent(
            DiscordApi api, Role role, int newPosition, int oldPosition) {
        super(api, role);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
    }

    /**
     * Gets the new position of the role.
     *
     * @return The new position of the role.
     */
    public int getNewPosition() {
        return newPosition;
    }

    /**
     * Gets the old position of the role.
     *
     * @return The old position of the role.
     */
    public int getOldPosition() {
        return oldPosition;
    }
}
