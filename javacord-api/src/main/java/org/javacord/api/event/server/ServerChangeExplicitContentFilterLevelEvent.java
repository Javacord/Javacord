package org.javacord.api.event.server;

import org.javacord.api.entity.server.ExplicitContentFilterLevel;

/**
 * A server change explicit content filter level event.
 */
public interface ServerChangeExplicitContentFilterLevelEvent extends ServerEvent {

    /**
     * Gets the old explicit content filter level of the server.
     *
     * @return The old explicit content filter level of the server.
     */
    ExplicitContentFilterLevel getOldExplicitContentFilterLevel();

    /**
     * Gets the new explicit content filter level of the server.
     *
     * @return The new explicit content filter level of the server.
     */
    ExplicitContentFilterLevel getNewExplicitContentFilterLevel();

}
