package de.btobastian.javacord.listeners.connection;

import de.btobastian.javacord.events.connection.ReconnectEvent;

/**
 * This listener listens to reconnected sessions.
 * Reconnecting a session, means that it's likely that events were lost and therefore all objects were replaced.
 */
@FunctionalInterface
public interface ReconnectListener {

    /**
     * This method is called every time a connection is reconnected.
     *
     * @param event The event.
     */
    void onReconnect(ReconnectEvent event);

}
