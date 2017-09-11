package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;

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
