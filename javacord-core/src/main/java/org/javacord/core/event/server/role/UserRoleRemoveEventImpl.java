package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.role.UserRoleRemoveEvent;

/**
 * The implementation of {@link UserRoleRemoveEvent}.
 */
public class UserRoleRemoveEventImpl extends UserRoleEventImpl implements UserRoleRemoveEvent {

    /**
     * Creates a new user role remove event.
     *
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleRemoveEventImpl(Role role, User user) {
        super(role, user);
    }

}
