package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeRegionEvent;

/**
 * This listener listens to server region changes.
 */
@FunctionalInterface
public interface ServerChangeRegionListener {

    /**
     * This method is called every time a server's region changed.
     *
     * @param event The event.
     */
    void onServerChangeRegion(ServerChangeRegionEvent event);
}
