package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
