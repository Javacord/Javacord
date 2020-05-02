package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeVanityUrlCodeEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server vanity url code changes.
 */
@FunctionalInterface
public interface ServerChangeVanityUrlCodeListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's vanity url code changed.
     *
     * @param event The event.
     */
    void onServerChangeVanityUrlCode(ServerChangeVanityUrlCodeEvent event);
}
