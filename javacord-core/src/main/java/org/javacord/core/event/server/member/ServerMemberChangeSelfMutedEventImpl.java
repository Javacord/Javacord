package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeSelfMutedEvent;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link ServerMemberChangeSelfMutedEvent}.
 */
public class ServerMemberChangeSelfMutedEventImpl extends ServerMemberEventImpl implements
        ServerMemberChangeSelfMutedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change self muted event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeSelfMutedEventImpl(Member newMember, Member oldMember) {
        super(newMember);
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
