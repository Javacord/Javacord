package org.javacord.event.server.member;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.ServerEvent;

/**
 * A server member event.
 */
public abstract class ServerMemberEvent extends ServerEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new server member event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberEvent(DiscordApi api, Server server, User user) {
        super(api, server);
        this.user = user;
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    public User getUser() {
        return user;
    }

}
