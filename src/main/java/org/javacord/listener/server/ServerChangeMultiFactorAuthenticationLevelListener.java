package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
