package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;

/**
 * The implementation of {@link ServerMemberJoinEvent}.
 */
public class ServerMemberJoinEventImpl extends ServerMemberEventImpl implements ServerMemberJoinEvent {

    /**
     * Creates a new server member join event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberJoinEventImpl(Server server, User user) {
        super(server, user);
    }

}
