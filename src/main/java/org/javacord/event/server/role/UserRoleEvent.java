package org.javacord.event.server.role;

import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;

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
    public User getUser() {
        return user;
    }
}
