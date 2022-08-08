package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeTimeoutEvent;
import java.time.Instant;
import java.util.Optional;

/**
 * The implementation of {@link ServerMemberChangeTimeoutEvent}.
 */
public class ServerMemberChangeTimeoutEventImpl extends ServerMemberEventImpl implements
        ServerMemberChangeTimeoutEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change timeout event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeTimeoutEventImpl(Member newMember, Member oldMember) {
        super(newMember);
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public Optional<Instant> getNewTimeout() {
        return newMember.getTimeout();
    }

    @Override
    public Optional<Instant> getOldTimeout() {
        return oldMember.getTimeout();
    }
}
