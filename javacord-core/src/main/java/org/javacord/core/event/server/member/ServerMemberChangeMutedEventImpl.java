package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeMutedEvent;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link ServerMemberChangeMutedEvent}.
 */
public class ServerMemberChangeMutedEventImpl extends ServerMemberEventImpl implements ServerMemberChangeMutedEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change muted event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeMutedEventImpl(Member newMember, Member oldMember) {
        super(newMember);
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public boolean isNewMuted() {
        return newMember.isSelfMuted();
    }

    @Override
    public boolean isOldMuted() {
        return !isNewMuted();
    }
}
