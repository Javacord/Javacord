package org.javacord.api.event.server.member;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerEvent;

import java.util.List;

/**
 * A server members chunk event.
 */
public interface ServerMembersChunkEvent extends ServerEvent {

    /**
     * Gets the users contained in this chunk.
     *
     * @return The users of this chunk.
     */
    List<User> getMembers();
}
