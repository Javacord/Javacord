package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.event.server.role.RoleChangePositionEvent;

/**
 * The implementation of {@link RoleChangePositionEvent}.
 */
public class ImplRoleChangePositionEvent extends ImplRoleEvent implements RoleChangePositionEvent {

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
     * @param role The role of the event.
     * @param oldPosition The old position of the role.
     * @param newPosition The new position of the role.
     */
    public ImplRoleChangePositionEvent(Role role, int newPosition, int oldPosition) {
        super(role);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
    }

    @Override
    public int getNewPosition() {
        return newPosition;
    }

    @Override
    public int getOldPosition() {
        return oldPosition;
    }
}
