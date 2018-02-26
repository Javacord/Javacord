package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeSystemChannelEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

/**
 * This listener listens to server system channel changes.
 */
@FunctionalInterface
public interface ServerChangeSystemChannelListener extends ServerAttachableListener, GloballyAttachableListener,
                                                           ObjectAttachableListener {

    /**
     * This method is called every time a server's system channel changed.
     *
     * @param event The event.
     */
    void onServerChangeSystemChannel(ServerChangeSystemChannelEvent event);
}
