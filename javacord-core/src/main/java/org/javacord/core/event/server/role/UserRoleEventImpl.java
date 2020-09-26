package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.role.UserRoleEvent;
import org.javacord.core.entity.user.Member;

/**
 * The implementation of {@link UserRoleEvent}.
 */
public abstract class UserRoleEventImpl extends RoleEventImpl implements UserRoleEvent {

    private final Member member;

    /**
     * Creates a new member role event.
     *
     * @param role The role of the event.
     * @param member The member of the event.
     */
    public UserRoleEventImpl(Role role, Member member) {
        super(role);
        this.member = member;
    }

    @Override
    public User getUser() {
        return member.getUser();
    }
}
