package org.javacord.api.event.server;

import org.javacord.api.entity.server.ServerFeature;

import java.util.Set;

/**
 * A server change server features event.
 */
public interface ServerChangeServerFeaturesEvent extends ServerEvent {

    /**
     * Gets the old server features of the server.
     *
     * @return The old server features of the server.
     */
    Set<ServerFeature> getOldServerFeatures();

    /**
     * Gets the new server features of the server.
     *
     * @return The new server features of the server.
     */
    Set<ServerFeature> getNewServerFeatures();

}
