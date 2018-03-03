package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;

/**
 * A server leave event.
 */
public class ServerLeaveEvent extends ServerEvent {

    /**
     * Creates a new server leave event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     */
    public ServerLeaveEvent(DiscordApi api, Server server) {
        super(api, server);
    }

}
