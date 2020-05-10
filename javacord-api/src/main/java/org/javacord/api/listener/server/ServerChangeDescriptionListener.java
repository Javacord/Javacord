package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeDescriptionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server description changes.
 */
@FunctionalInterface
public interface ServerChangeDescriptionListener  extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's description changed.
     *
     * @param event The event.
     */
    void onServerChangeDescription(ServerChangeDescriptionEvent event);
}
