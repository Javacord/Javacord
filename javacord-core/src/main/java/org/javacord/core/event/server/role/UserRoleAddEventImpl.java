package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.role.UserRoleAddEvent;

/**
 * The implementation of {@link UserRoleAddEvent}.
 */
public class UserRoleAddEventImpl extends UserRoleEventImpl implements UserRoleAddEvent {

    /**
     * Creates a new user role add event.
     *
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleAddEventImpl(Role role, User user) {
        super(role, user);
    }

}
