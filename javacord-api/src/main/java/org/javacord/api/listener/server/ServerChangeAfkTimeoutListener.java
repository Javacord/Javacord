package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeAfkTimeoutEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

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
