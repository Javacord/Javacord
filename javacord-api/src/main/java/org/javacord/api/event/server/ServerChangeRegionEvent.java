package org.javacord.api.event.server;

import org.javacord.api.entity.Region;

/**
 * A server change region event.
 */
public interface ServerChangeRegionEvent extends ServerEvent {

    /**
     * Gets the old region of the server.
     *
     * @return The old region of the server.
     */
    Region getOldRegion();

    /**
     * Gets the new region of the server.
     *
     * @return The new region of the server.
     */
    Region getNewRegion();

}
