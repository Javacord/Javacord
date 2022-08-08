package org.javacord.api.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server member event.
 */
public interface ServerMemberEvent extends ServerEvent {
    /**
     * Gets the member of the event.
     *
     * @return The member of the event.
     */
    Member getMember();
}
