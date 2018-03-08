package org.javacord.event.server.member.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberUnbanEvent;

/**
 * The implementation of {@link ServerMemberUnbanEvent}.
 */
public class ImplServerMemberUnbanEvent extends ImplServerMemberEvent implements ServerMemberUnbanEvent {

    /**
     * Creates a new server member unban event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ImplServerMemberUnbanEvent(Server server, User user) {
        super(server, user);
    }

}
