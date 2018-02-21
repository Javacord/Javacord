package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.permissions.Role;

import java.awt.Color;
import java.util.Optional;

/**
 * A role change color event.
 */
public class RoleChangeColorEvent extends RoleEvent {

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
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param newColor The new color of the role.
     * @param oldColor The old color of the role.
     */
    public RoleChangeColorEvent(DiscordApi api, Role role, Color newColor, Color oldColor) {
        super(api, role);
        this.newColor = newColor;
        this.oldColor = oldColor;
    }

    /**
     * Gets the old color of the role.
     *
     * @return The old color of the role.
     */
    public Optional<Color> getOldColor() {
        return Optional.ofNullable(oldColor);
    }

    /**
     * Gets the new color of the role.
     *
     * @return The new color of the role.
     */
    public Optional<Color> getNewColor() {
        return Optional.ofNullable(newColor);
    }
}
