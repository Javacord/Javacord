package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

/**
 * A server member remove event.
 */
public class ServerMemberRemoveEvent extends ServerMemberEvent {

    /**
     * Creates a new server member remove event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberRemoveEvent(DiscordApi api, Server server, User user) {
        super(api, server, user);
    }

}
