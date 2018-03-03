package org.javacord.event.server.role;

import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;

/**
 * A user role remove event.
 */
public class UserRoleRemoveEvent extends UserRoleEvent {

    /**
     * Creates a new user role remove event.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleRemoveEvent(DiscordApi api, Role role, User user) {
        super(api, role, user);
    }

}
