package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeAfkChannelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

/**
 * This listener listens to server afk channel changes.
 */
@FunctionalInterface
public interface ServerChangeAfkChannelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's afk channel changed.
     *
     * @param event The event.
     */
    void onServerChangeAfkChannel(ServerChangeAfkChannelEvent event);
}
