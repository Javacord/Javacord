package org.javacord.api.event.server;

/**
 * A server change name event.
 */
public interface ServerChangeNameEvent extends ServerEvent {

    /**
     * Gets the old name of the server.
     *
     * @return The old name of the server.
     */
    String getOldName();

    /**
     * Gets the new name of the server.
     *
     * @return The new name of the server.
     */
    String getNewName();

}
