package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberUnbanEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerMemberUnbanEvent}.
 */
public class ServerMemberUnbanEventImpl extends ServerEventImpl implements ServerMemberUnbanEvent {

    private final User user;

    /**
     * Creates a new server member unban event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberUnbanEventImpl(Server server, User user) {
        super(server);
        this.user = user;
    }


    @Override
    public User getUser() {
        return user;
    }
}
