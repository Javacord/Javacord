package org.javacord.core.event.user;

import org.javacord.api.event.user.UserChangeSelfMutedEvent;
import org.javacord.core.entity.user.Member;

/**
 * The implementation of {@link UserChangeSelfMutedEvent}.
 */
public class UserChangeSelfMutedEventImpl extends ServerUserEventImpl implements UserChangeSelfMutedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change self muted event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public UserChangeSelfMutedEventImpl(Member newMember, Member oldMember) {
        super(newMember.getUser(), newMember.getServer());
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public boolean isNewSelfMuted() {
        return newMember.isSelfMuted();
    }

    @Override
    public boolean isOldSelfMuted() {
        return oldMember.isSelfMuted();
    }
}
