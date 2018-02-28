package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeSplashEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
