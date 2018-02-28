package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeNameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
