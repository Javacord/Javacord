package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeAfkChannelEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

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
