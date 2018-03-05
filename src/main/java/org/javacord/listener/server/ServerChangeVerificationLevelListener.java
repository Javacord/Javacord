package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeVerificationLevelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

/**
 * This listener listens to server verification level changes.
 */
@FunctionalInterface
public interface ServerChangeVerificationLevelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's verification level changed.
     *
     * @param event The event.
     */
    void onServerChangeVerificationLevel(ServerChangeVerificationLevelEvent event);
}
