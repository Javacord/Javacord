package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.core.event.user.ServerUserEventImpl;

/**
 * The implementation of {@link ServerMemberLeaveEvent}.
 */
public class ServerMemberLeaveEventImpl extends ServerUserEventImpl implements ServerMemberLeaveEvent {

    /**
     * Creates a new server member leave event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberLeaveEventImpl(Server server, User user) {
        super(user, server);
    }

}
