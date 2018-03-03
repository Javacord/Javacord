package org.javacord.event.server.role;

import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;

/**
 * A user role add event.
 */
public class UserRoleAddEvent extends UserRoleEvent {

    /**
     * Creates a new user role add event.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleAddEvent(DiscordApi api, Role role, User user) {
        super(api, role, user);
    }

}
