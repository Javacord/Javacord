package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;

/**
 * A server change name event.
 */
public class ServerChangeNameEvent extends ServerEvent {

    /**
     * The new name of the server.
     */
    private final String newName;

    /**
     * The old name of the server.
     */
    private final String oldName;

    /**
     * Creates a new server change name event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newName The new name of the server.
     * @param oldName The old name of the server.
     */
    public ServerChangeNameEvent(DiscordApi api, Server server, String newName, String oldName) {
        super(api, server);
        this.newName = newName;
        this.oldName = oldName;
    }

    /**
     * Gets the old name of the server.
     *
     * @return The old name of the server.
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Gets the new name of the server.
     *
     * @return The new name of the server.
     */
    public String getNewName() {
        return newName;
    }

}
