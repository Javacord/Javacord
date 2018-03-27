package org.javacord.core.event.server;

import org.javacord.api.entity.Region;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeRegionEvent;

/**
 * The implementation of {@link ServerChangeRegionEvent}.
 */
public class ServerChangeRegionEventImpl extends ServerEventImpl implements ServerChangeRegionEvent {

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
     * @param server The server of the event.
     * @param newRegion The new region of the server.
     * @param oldRegion The old region of the server.
     */
    public ServerChangeRegionEventImpl(Server server, Region newRegion, Region oldRegion) {
        super(server);
        this.newRegion = newRegion;
        this.oldRegion = oldRegion;
    }

    @Override
    public Region getOldRegion() {
        return oldRegion;
    }

    @Override
    public Region getNewRegion() {
        return newRegion;
    }

}
