package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangePendingEvent;

/**
 * The implementation of {@link ServerMemberChangePendingEvent}.
 */
public class ServerMemberChangePendingEventImpl extends ServerMemberEventImpl
        implements ServerMemberChangePendingEvent {

    /**
     * The old pending state of the member.
     */
    private final boolean oldPending;

    /**
     * The new pending state of the member.
     */
    private final boolean newPending;

    /**
     * Creates a new server member pending change event.
     *
     * @param oldMember The old member.
     * @param newMember The new member.
     */
    public ServerMemberChangePendingEventImpl(Member oldMember, Member newMember) {
        super(newMember);
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
