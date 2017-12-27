package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeIconEvent;

/**
 * This listener listens to server icon changes.
 */
@FunctionalInterface
public interface ServerChangeIconListener {

    /**
     * This method is called every time a server's icon changed.
     *
     * @param event The event.
     */
    void onServerChangeIcon(ServerChangeIconEvent event);
}
