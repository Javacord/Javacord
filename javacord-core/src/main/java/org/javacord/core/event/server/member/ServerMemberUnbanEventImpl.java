package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberUnbanEvent;

/**
 * The implementation of {@link ServerMemberUnbanEvent}.
 */
public class ServerMemberUnbanEventImpl extends ServerMemberEventImpl implements ServerMemberUnbanEvent {

    /**
     * Creates a new server member unban event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberUnbanEventImpl(Server server, User user) {
        super(server, user);
    }

}
