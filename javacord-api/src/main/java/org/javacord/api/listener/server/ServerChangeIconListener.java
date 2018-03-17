package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeIconEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server icon changes.
 */
@FunctionalInterface
public interface ServerChangeIconListener extends ServerAttachableListener, GloballyAttachableListener,
                                                  ObjectAttachableListener {

    /**
     * This method is called every time a server's icon changed.
     *
     * @param event The event.
     */
    void onServerChangeIcon(ServerChangeIconEvent event);
}
