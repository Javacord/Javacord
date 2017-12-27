package de.btobastian.javacord.events.server.member;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

/**
 * A server member join event.
 */
public class ServerMemberJoinEvent extends ServerMemberEvent {

    /**
     * Creates a new server member join event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberJoinEvent(DiscordApi api, Server server, User user) {
        super(api, server, user);
    }

}
