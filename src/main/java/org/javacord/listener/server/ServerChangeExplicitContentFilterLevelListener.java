package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
