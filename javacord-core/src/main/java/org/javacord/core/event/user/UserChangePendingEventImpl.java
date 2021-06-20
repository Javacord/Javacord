package org.javacord.core.event.user;

import org.javacord.api.event.user.UserChangePendingEvent;
import org.javacord.core.entity.user.Member;
import org.javacord.core.event.user.ServerUserEventImpl;

/**
 * The implementation of {@link UserChangePendingEvent}.
 */
public class UserChangePendingEventImpl extends ServerUserEventImpl
        implements UserChangePendingEvent {

    /**
     * The old pending state of the user.
     */
    private final boolean oldPending;

    /**
     * The new pending state of the user.
     */
    private final boolean newPending;

    /**
     * Creates a new server member pending change event.
     *
     * @param oldMember The old member.
     * @param newMember The new member.
     */
    public UserChangePendingEventImpl(Member oldMember, Member newMember) {
        super(newMember.getUser(), newMember.getServer());
        this.oldPending = oldMember.isPending();
        this.newPending = newMember.isPending();
    }

    @Override
    public boolean getOldPending() {
        return oldPending;
    }

    @Override
    public boolean getNewPending() {
        return newPending;
    }
}
