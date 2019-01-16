package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleChangePositionEvent;

/**
 * The implementation of {@link RoleChangePositionEvent}.
 */
public class RoleChangePositionEventImpl extends RoleEventImpl implements RoleChangePositionEvent {

    /**
     * The new position of the role.
     */
    private final int newPosition;

    /**
     * The old position of the role.
     */
    private final int oldPosition;

    /**
     * The new raw position of the role.
     */
    private final int newRawPosition;

    /**
     * The old raw position of the role.
     */
    private final int oldRawPosition;

    /**
     * Creates a new role change color event.
     *
     * @param role The role of the event.
     * @param newPosition The new position of the role.
     * @param oldPosition The old position of the role.
     * @param newRawPosition The new raw position of the role.
     * @param oldRawPosition The old raw position of the role.
     */
    public RoleChangePositionEventImpl(Role role, int newPosition, int oldPosition,
                                                    int newRawPosition, int oldRawPosition) {
        super(role);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
        this.newRawPosition = newRawPosition;
        this.oldRawPosition = oldRawPosition;
    }

    @Override
    public int getNewPosition() {
        return newPosition;
    }

    @Override
    public int getOldPosition() {
        return oldPosition;
    }

    @Override
    public int getNewRawPosition() {
        return newRawPosition;
    }

    @Override
    public int getOldRawPosition() {
        return oldRawPosition;
    }
}
