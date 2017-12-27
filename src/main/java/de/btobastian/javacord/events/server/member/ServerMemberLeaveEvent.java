package de.btobastian.javacord.events.server.member;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

/**
 * A server member leave event.
 */
public class ServerMemberLeaveEvent extends ServerMemberEvent {

    /**
     * Creates a new server member leave event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberLeaveEvent(DiscordApi api, Server server, User user) {
        super(api, server, user);
    }

}
