package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeNameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

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
