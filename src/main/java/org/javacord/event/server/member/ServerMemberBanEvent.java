package org.javacord.event.server.member;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;

/**
 * A server member ban event.
 */
public class ServerMemberBanEvent extends ServerMemberEvent {

    /**
     * Creates a new server member ban event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberBanEvent(DiscordApi api, Server server, User user) {
        super(api, server, user);
    }

}
