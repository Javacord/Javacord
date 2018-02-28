package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeSystemChannelEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
