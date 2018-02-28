package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeIconEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
