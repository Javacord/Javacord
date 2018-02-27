package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeVerificationLevelEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
