package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeVerificationLevelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

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
