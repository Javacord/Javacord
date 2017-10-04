package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;

import java.awt.*;

/**
 * A role change color event.
 */
public class RoleChangeColorEvent extends RoleEvent {

    /**
     * The old color value.
     */
    private final Color oldColor;

    /**
     * Creates a new role change color event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param oldColor The old color of the role.
     */
    public RoleChangeColorEvent(DiscordApi api, Server server, Role role, Color oldColor) {
        super(api, server, role);
        this.oldColor = oldColor;
    }

    /**
     * Gets the old color of the role.
     *
     * @return The old color of the role.
     */
    public Color getOldColor() {
        return oldColor;
    }

    /**
     * Gets the new color of the role.
     *
     * @return The new color of the role.
     */
    public Color getNewColor() {
        // TODO: return getRole().getColor();
        return null;
    }
}
