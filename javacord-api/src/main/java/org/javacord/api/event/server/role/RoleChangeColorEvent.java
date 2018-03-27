package org.javacord.api.event.server.role;

import java.awt.Color;
import java.util.Optional;

/**
 * A role change color event.
 */
public interface RoleChangeColorEvent extends RoleEvent {

    /**
     * Gets the old color of the role.
     *
     * @return The old color of the role.
     */
    Optional<Color> getOldColor();

    /**
     * Gets the new color of the role.
     *
     * @return The new color of the role.
     */
    Optional<Color> getNewColor();

}
