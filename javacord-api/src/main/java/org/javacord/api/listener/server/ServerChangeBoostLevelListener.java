package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeBoostLevelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server boost level changes.
 */
@FunctionalInterface
public interface ServerChangeBoostLevelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's boost level changed.
     *
     * @param event The event.
     */
    void onServerChangeBoostLevel(ServerChangeBoostLevelEvent event);
}
