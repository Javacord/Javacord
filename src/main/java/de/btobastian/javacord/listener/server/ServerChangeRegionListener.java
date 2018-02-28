package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeRegionEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
