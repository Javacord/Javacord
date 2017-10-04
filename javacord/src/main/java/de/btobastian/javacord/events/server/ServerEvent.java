package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.events.Event;

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
