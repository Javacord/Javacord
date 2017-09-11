package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeRegionEvent;

/**
 * This listener listens to server region changes.
 */
@FunctionalInterface
public interface ServerChangeRegionListener {

    /**
     * This method is called every time a server changes region.
     *
     * @param event The event.
     */
    void onServerChangeRegion(ServerChangeRegionEvent event);
}
