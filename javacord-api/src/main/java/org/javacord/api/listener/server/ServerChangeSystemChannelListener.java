package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeSystemChannelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

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
