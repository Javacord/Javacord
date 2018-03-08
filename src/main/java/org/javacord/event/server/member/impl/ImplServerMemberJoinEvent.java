package org.javacord.event.server.member.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberJoinEvent;

/**
 * The implementation of {@link ServerMemberJoinEvent}.
 */
public class ImplServerMemberJoinEvent extends ImplServerMemberEvent implements ServerMemberJoinEvent {

    /**
     * Creates a new server member join event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ImplServerMemberJoinEvent(Server server, User user) {
        super(server, user);
    }

}
