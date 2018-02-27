package de.btobastian.javacord.event.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.server.Server;

/**
 * A server join event.
 */
public class ServerJoinEvent extends ServerEvent {

    /**
     * Creates a new server join event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     */
    public ServerJoinEvent(DiscordApi api, Server server) {
        super(api, server);
    }

}
