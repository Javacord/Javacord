package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.event.Event;

/**
 * A server event.
 */
public abstract class ServerEvent extends Event {

    /**
     * The server of the event.
     */
    private final Server server;

    /**
     * Creates a new server event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     */
    public ServerEvent(DiscordApi api, Server server) {
        super(api);
        this.server = server;
    }

    /**
     * Gets the server of the event.
     *
     * @return The server of the event.
     */
    public Server getServer() {
        return server;
    }

}
