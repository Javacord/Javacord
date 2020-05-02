package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeDiscoverySplashEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server discovery splash changes.
 */
@FunctionalInterface
public interface ServerChangeDiscoverySplashListener  extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's discovery splash changed.
     *
     * @param event The event.
     */
    void onServerChangeDiscoverySplash(ServerChangeDiscoverySplashEvent event);
}
