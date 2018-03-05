package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeSystemChannelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
