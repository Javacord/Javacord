package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeNameEvent;

/**
 * This listener listens to server name changes.
 */
@FunctionalInterface
public interface ServerChangeNameListener {

    /**
     * This method is called every time a server's name changed.
     *
     * @param event The event.
     */
    void onServerChangeName(ServerChangeNameEvent event);
}
