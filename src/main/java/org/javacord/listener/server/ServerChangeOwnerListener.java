package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeOwnerEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
