package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeExplicitContentFilterLevelEvent;

/**
 * This listener listens to server explicit content filter level changes.
 */
@FunctionalInterface
public interface ServerChangeExplicitContentFilterLevelListener {

    /**
     * This method is called every time a server's explicit content filter level changed.
     *
     * @param event The event.
     */
    void onServerChangeExplicitContentFilterLevel(ServerChangeExplicitContentFilterLevelEvent event);
}
