package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeIconEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

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
