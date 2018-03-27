package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeRegionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server region changes.
 */
@FunctionalInterface
public interface ServerChangeRegionListener extends ServerAttachableListener, GloballyAttachableListener,
                                                    ObjectAttachableListener {

    /**
     * This method is called every time a server's region changed.
     *
     * @param event The event.
     */
    void onServerChangeRegion(ServerChangeRegionEvent event);
}
