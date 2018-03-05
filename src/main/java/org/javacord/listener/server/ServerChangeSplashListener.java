package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeSplashEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
