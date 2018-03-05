package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

/**
 * This listener listens to server name changes.
 */
@FunctionalInterface
public interface ServerChangeNameListener extends ServerAttachableListener, GloballyAttachableListener,
                                                  ObjectAttachableListener {

    /**
     * This method is called every time a server's name changed.
     *
     * @param event The event.
     */
    void onServerChangeName(ServerChangeNameEvent event);
}
