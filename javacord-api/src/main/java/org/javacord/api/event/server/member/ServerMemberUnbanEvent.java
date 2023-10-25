package org.javacord.api.event.server.member;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server member unban event.
 */
public interface ServerMemberUnbanEvent extends ServerEvent {

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    User getUser();

}
