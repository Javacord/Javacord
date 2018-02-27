package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeOwnerEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
