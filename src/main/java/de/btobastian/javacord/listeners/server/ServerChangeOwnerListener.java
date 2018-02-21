package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeOwnerEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

/**
 * This listener listens to server owner changes.
 */
@FunctionalInterface
public interface ServerChangeOwnerListener extends ServerAttachableListener, GloballyAttachableListener,
                                                   ObjectAttachableListener {

    /**
     * This method is called every time a server's owner changed.
     *
     * @param event The event.
     */
    void onServerChangeOwner(ServerChangeOwnerEvent event);
}
