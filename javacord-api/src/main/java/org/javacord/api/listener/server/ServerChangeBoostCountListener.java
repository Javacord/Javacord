package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeBoostCountEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server boost count changes.
 */
@FunctionalInterface
public interface ServerChangeBoostCountListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's boost count changed.
     *
     * @param event The event.
     */
    void onServerChangeBoostCount(ServerChangeBoostCountEvent event);
}
