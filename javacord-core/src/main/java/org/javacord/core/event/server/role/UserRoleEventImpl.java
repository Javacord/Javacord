package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.role.UserRoleEvent;

/**
 * The implementation of {@link UserRoleEvent}.
 */
public abstract class UserRoleEventImpl extends RoleEventImpl implements UserRoleEvent {

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
    public UserRoleEventImpl(Role role, User user) {
        super(role);
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
