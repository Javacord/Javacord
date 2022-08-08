package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeSelfDeafenedEvent;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link ServerMemberChangeSelfDeafenedEvent}.
 */
public class ServerMemberChangeSelfDeafenedEventImpl extends ServerMemberEventImpl implements
        ServerMemberChangeSelfDeafenedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new member change self deafened event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeSelfDeafenedEventImpl(Member newMember, Member oldMember) {
        super(newMember);
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
