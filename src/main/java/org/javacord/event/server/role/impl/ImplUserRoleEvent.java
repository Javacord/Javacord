package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.event.server.role.UserRoleEvent;

/**
 * The implementation of {@link UserRoleEvent}.
 */
public abstract class ImplUserRoleEvent extends ImplRoleEvent implements UserRoleEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new user role event.
     *
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public ImplUserRoleEvent(Role role, User user) {
        super(role);
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
