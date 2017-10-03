package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
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
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleEvent(DiscordApi api, Role role, User user) {
        super(api, role);
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
