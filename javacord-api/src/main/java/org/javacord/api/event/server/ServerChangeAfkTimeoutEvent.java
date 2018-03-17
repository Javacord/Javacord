package org.javacord.api.event.server;

/**
 * A server change afk timeout event.
 */
public interface ServerChangeAfkTimeoutEvent extends ServerEvent {

    /**
     * Gets the old afk timeout of the server in seconds.
     *
     * @return The old afk timeout of the server in seconds.
     */
    int getOldAfkTimeoutInSeconds();

    /**
     * Gets the new afk timeout of the server in seconds.
     *
     * @return The new afk timeout of the server in seconds.
     */
    int getNewAfkTimeoutInSeconds();

}
