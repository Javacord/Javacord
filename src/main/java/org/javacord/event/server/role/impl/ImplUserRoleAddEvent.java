package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.event.server.role.UserRoleAddEvent;

/**
 * The implementation of {@link UserRoleAddEvent}.
 */
public class ImplUserRoleAddEvent extends ImplUserRoleEvent implements UserRoleAddEvent {

    /**
     * Creates a new user role add event.
     *
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public ImplUserRoleAddEvent(Role role, User user) {
        super(role, user);
    }

}
