package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeAfkTimeoutEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

/**
 * This listener listens to server afk timeout changes.
 */
@FunctionalInterface
public interface ServerChangeAfkTimeoutListener extends ServerAttachableListener, GloballyAttachableListener,
                                                  ObjectAttachableListener {

    /**
     * This method is called every time a server's afk timeout changed.
     *
     * @param event The event.
     */
    void onServerChangeAfkTimeout(ServerChangeAfkTimeoutEvent event);
}
