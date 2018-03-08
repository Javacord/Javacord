package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.event.server.role.UserRoleRemoveEvent;

/**
 * The implementation of {@link UserRoleRemoveEvent}.
 */
public class ImplUserRoleRemoveEvent extends ImplUserRoleEvent implements UserRoleRemoveEvent {

    /**
     * Creates a new user role remove event.
     *
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public ImplUserRoleRemoveEvent(Role role, User user) {
        super(role, user);
    }

}
