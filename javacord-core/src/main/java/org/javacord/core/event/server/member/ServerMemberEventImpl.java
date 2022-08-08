package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerMemberEvent}.
 */
public abstract class ServerMemberEventImpl extends ServerEventImpl implements ServerMemberEvent {

    /**
     * The user of the event.
     */
    private final Member member;

    /**
     * Creates a new server member event.
     *
     * @param member The user of the event.
     */
    public ServerMemberEventImpl(Member member) {
        super(member.getServer());
        this.member = member;
    }

    @Override
    public Member getMember() {
        return member;
    }

}
