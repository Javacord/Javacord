package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeVerificationLevelEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

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
