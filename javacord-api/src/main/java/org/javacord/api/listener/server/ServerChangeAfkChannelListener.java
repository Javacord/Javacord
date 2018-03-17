package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeAfkChannelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

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
