package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeIconEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.event.server.ServerChangeIconEvent;

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
