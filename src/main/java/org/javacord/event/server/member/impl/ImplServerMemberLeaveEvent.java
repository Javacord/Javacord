package org.javacord.event.server.member.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberLeaveEvent;

/**
 * The implementation of {@link ServerMemberLeaveEvent}.
 */
public class ImplServerMemberLeaveEvent extends ImplServerMemberEvent implements ServerMemberLeaveEvent {

    /**
     * Creates a new server member leave event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ImplServerMemberLeaveEvent(Server server, User user) {
        super(server, user);
    }

}
