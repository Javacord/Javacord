package org.javacord.core.event.user;

import org.javacord.api.entity.user.Member;
import org.javacord.api.event.user.UserChangeDeafenedEvent;

/**
 * The implementation of {@link UserChangeDeafenedEvent}.
 */
public class UserChangeDeafenedEventImpl extends ServerUserEventImpl implements UserChangeDeafenedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change deafened event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public UserChangeDeafenedEventImpl(Member newMember, Member oldMember) {
        super(newMember.getUser(), newMember.getServer());
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public boolean isNewDeafened() {
        // TODO This is wrong.
        return newMember.isSelfDeafened();
    }

    @Override
    public boolean isOldDeafened() {
        return oldMember.isSelfDeafened();
    }
}
