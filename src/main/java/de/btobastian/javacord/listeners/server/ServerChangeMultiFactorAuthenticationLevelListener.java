package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

/**
 * This listener listens to server multi factor authentication level changes.
 */
@FunctionalInterface
public interface ServerChangeMultiFactorAuthenticationLevelListener extends ServerAttachableListener,
                                                                            GloballyAttachableListener,
                                                                            ObjectAttachableListener {

    /**
     * This method is called every time a server's multi factor authentication level changed.
     *
     * @param event The event.
     */
    void onServerChangeMultiFactorAuthenticationLevel(ServerChangeMultiFactorAuthenticationLevelEvent event);
}
