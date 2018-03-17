package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeSplashEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server splash changes.
 */
@FunctionalInterface
public interface ServerChangeSplashListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's splash changed.
     *
     * @param event The event.
     */
    void onServerChangeSplash(ServerChangeSplashEvent event);
}
