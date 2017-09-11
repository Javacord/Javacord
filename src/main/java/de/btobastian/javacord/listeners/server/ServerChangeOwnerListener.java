package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeOwnerEvent;

/**
 * This listener listens to server owner changes.
 */
@FunctionalInterface
public interface ServerChangeOwnerListener {

    /**
     * This method is called every time a server changes owner.
     *
     * @param event The event.
     */
    void onServerChangeOwner(ServerChangeOwnerEvent event);
}
