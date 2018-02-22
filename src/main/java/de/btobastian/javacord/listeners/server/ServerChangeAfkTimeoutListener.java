package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeAfkTimeoutEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

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
