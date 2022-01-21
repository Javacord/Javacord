package org.javacord.core.event.user;

import org.javacord.api.event.user.UserChangeTimeoutEvent;
import org.javacord.core.entity.user.Member;
import java.time.Instant;
import java.util.Optional;

/**
 * The implementation of {@link UserChangeTimeoutEvent}.
 */
public class UserChangeTimeoutEventImpl extends ServerUserEventImpl implements UserChangeTimeoutEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change timeout event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public UserChangeTimeoutEventImpl(Member newMember, Member oldMember) {
        super(newMember.getUser(), newMember.getServer());
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
