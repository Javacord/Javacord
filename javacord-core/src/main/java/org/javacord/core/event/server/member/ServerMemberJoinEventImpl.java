package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;

/**
 * The implementation of {@link ServerMemberJoinEvent}.
 */
public class ServerMemberJoinEventImpl extends ServerMemberEventImpl implements ServerMemberJoinEvent {

    /**
     * Creates a new server member join event.
     *
     * @param member The user of the event.
     */
    public ServerMemberJoinEventImpl(Member member) {
        super(member);
    }

}
