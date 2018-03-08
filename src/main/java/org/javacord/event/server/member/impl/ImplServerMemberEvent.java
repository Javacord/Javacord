package org.javacord.event.server.member.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.impl.ImplServerEvent;
import org.javacord.event.server.member.ServerMemberEvent;

/**
 * The implementation of {@link ServerMemberEvent}.
 */
public abstract class ImplServerMemberEvent extends ImplServerEvent implements ServerMemberEvent {

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
    public ImplServerMemberEvent(Server server, User user) {
        super(server);
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

}
