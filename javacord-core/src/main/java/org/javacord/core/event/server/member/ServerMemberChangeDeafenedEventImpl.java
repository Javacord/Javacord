package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeDeafenedEvent;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link ServerMemberChangeDeafenedEvent}.
 */
public class ServerMemberChangeDeafenedEventImpl extends ServerMemberEventImpl implements
        ServerMemberChangeDeafenedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change deafened event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeDeafenedEventImpl(Member newMember, Member oldMember) {
        super(newMember);
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
