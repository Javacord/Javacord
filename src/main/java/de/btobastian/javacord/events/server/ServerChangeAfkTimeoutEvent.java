package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;

/**
 * A server change afk timeout event.
 */
public class ServerChangeAfkTimeoutEvent extends ServerEvent {

    /**
     * The new afk timeout of the server.
     */
    private final int newAfkTimeout;

    /**
     * The old afk timeout of the server.
     */
    private final int oldAfkTimeout;

    /**
     * Creates a new server change afk timeout event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newAfkTimeout The new afk timeout of the server.
     * @param oldAfkTimeout The old afk timeout of the server.
     */
    public ServerChangeAfkTimeoutEvent(DiscordApi api, Server server, int newAfkTimeout, int oldAfkTimeout) {
        super(api, server);
        this.newAfkTimeout = newAfkTimeout;
        this.oldAfkTimeout = oldAfkTimeout;
    }

    /**
     * Gets the old afk timeout of the server in seconds.
     *
     * @return The old afk timeout of the server in seconds.
     */
    public int getOldAfkTimeoutInSeconds() {
        return oldAfkTimeout;
    }

    /**
     * Gets the new afk timeout of the server in seconds.
     *
     * @return The new afk timeout of the server in seconds.
     */
    public int getNewAfkTimeoutInSeconds() {
        return newAfkTimeout;
    }

}
