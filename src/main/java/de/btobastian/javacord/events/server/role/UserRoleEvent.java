package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A user role event.
 */
public abstract class UserRoleEvent extends RoleEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new user role event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleEvent(DiscordApi api, Server server, Role role, User user) {
        super(api, server, role);
        this.user = user;
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    public User getuser() {
        return user;
    }
}
