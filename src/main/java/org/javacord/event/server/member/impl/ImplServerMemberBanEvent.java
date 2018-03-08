package org.javacord.event.server.member.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberBanEvent;

/**
 * The implementation of {@link ServerMemberBanEvent}.
 */
public class ImplServerMemberBanEvent extends ImplServerMemberEvent implements ServerMemberBanEvent {

    /**
     * Creates a new server member ban event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ImplServerMemberBanEvent(Server server, User user) {
        super(server, user);
    }

}
