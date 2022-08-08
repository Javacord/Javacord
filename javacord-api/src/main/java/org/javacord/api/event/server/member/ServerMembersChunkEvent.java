package org.javacord.api.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.ServerEvent;
import java.util.List;

/**
 * A server members chunk event.
 */
public interface ServerMembersChunkEvent extends ServerEvent {

    /**
     * Gets the members contained in this chunk.
     *
     * @return The members of this chunk.
     */
    List<Member> getMembers();
}
