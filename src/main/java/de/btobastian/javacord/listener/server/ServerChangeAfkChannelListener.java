package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeAfkChannelEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
