package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeSplashEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

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
