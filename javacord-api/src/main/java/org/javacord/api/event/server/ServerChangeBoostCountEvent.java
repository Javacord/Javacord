package org.javacord.api.event.server;

/**
 * A server change boost count event.
 */
public interface ServerChangeBoostCountEvent extends ServerEvent {

    /**
     * Gets the old boost count of the server.
     *
     * @return The old boost count of the server.
     */
    int getOldBoostCount();

    /**
     * Gets the new boost count of the server.
     *
     * @return The new boost count of the server.
     */
    int getNewBoostCount();

}
