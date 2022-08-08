package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberEvent;
import org.javacord.api.event.server.member.ServerUserEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerMemberEvent}.
 */
public abstract class ServerUserEventImpl extends ServerEventImpl implements ServerUserEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new server member event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerUserEventImpl(Server server, User user) {
        super(server);
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

}
