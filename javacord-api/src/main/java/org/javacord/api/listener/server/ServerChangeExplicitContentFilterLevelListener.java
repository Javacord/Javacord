package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server explicit content filter level changes.
 */
@FunctionalInterface
public interface ServerChangeExplicitContentFilterLevelListener extends ServerAttachableListener,
        GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's explicit content filter level changed.
     *
     * @param event The event.
     */
    void onServerChangeExplicitContentFilterLevel(ServerChangeExplicitContentFilterLevelEvent event);
}
