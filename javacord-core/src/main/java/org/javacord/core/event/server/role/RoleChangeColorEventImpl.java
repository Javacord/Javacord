package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleChangeColorEvent;

import java.awt.Color;
import java.util.Optional;

/**
 * The implementation of {@link RoleChangeColorEvent}.
 */
public class RoleChangeColorEventImpl extends RoleEventImpl implements RoleChangeColorEvent {

    /**
     * The new color value.
     */
    private final Color newColor;

    /**
     * The old color value.
     */
    private final Color oldColor;

    /**
     * Creates a new role change color event.
     *
     * @param role The role of the event.
     * @param newColor The new color of the role.
     * @param oldColor The old color of the role.
     */
    public RoleChangeColorEventImpl(Role role, Color newColor, Color oldColor) {
        super(role);
        this.newColor = newColor;
        this.oldColor = oldColor;
    }

    @Override
    public Optional<Color> getOldColor() {
        return Optional.ofNullable(oldColor);
    }

    @Override
    public Optional<Color> getNewColor() {
        return Optional.ofNullable(newColor);
    }
}
