package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeRegionEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.event.server.ServerChangeRegionEvent;

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
