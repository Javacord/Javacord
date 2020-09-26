package org.javacord.core.event.user;

import org.javacord.api.event.user.UserChangeSelfDeafenedEvent;
import org.javacord.core.entity.user.Member;

/**
 * The implementation of {@link UserChangeSelfDeafenedEvent}.
 */
public class UserChangeSelfDeafenedEventImpl extends ServerUserEventImpl implements UserChangeSelfDeafenedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change self deafened event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public UserChangeSelfDeafenedEventImpl(Member newMember, Member oldMember) {
        super(newMember.getUser(), newMember.getServer());
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public boolean isNewSelfDeafened() {
        return newMember.isSelfDeafened();
    }

    @Override
    public boolean isOldSelfDeafened() {
        return oldMember.isSelfDeafened();
    }
}
