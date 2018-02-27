package de.btobastian.javacord.event.server.member;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.event.server.ServerEvent;

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
