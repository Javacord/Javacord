package de.btobastian.javacord.event.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.Region;
import de.btobastian.javacord.entity.server.Server;

/**
 * A server change region event.
 */
public class ServerChangeRegionEvent extends ServerEvent {

    /**
     * The new region of the server.
     */
    private final Region newRegion;

    /**
     * The old region of the server.
     */
    private final Region oldRegion;

    /**
     * Creates a new server change region event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newRegion The new region of the server.
     * @param oldRegion The old region of the server.
     */
    public ServerChangeRegionEvent(DiscordApi api, Server server, Region newRegion, Region oldRegion) {
        super(api, server);
        this.newRegion = newRegion;
        this.oldRegion = oldRegion;
    }

    /**
     * Gets the old region of the server.
     *
     * @return The old region of the server.
     */
    public Region getOldRegion() {
        return oldRegion;
    }

    /**
     * Gets the new region of the server.
     *
     * @return The new region of the server.
     */
    public Region getNewRegion() {
        return newRegion;
    }

}
