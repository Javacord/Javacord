package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeOwnerEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

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
