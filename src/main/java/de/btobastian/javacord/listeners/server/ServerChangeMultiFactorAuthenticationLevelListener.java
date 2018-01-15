package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeMultiFactorAuthenticationLevelEvent;

/**
 * This listener listens to server multi factor authentication level changes.
 */
@FunctionalInterface
public interface ServerChangeMultiFactorAuthenticationLevelListener {

    /**
     * This method is called every time a server's multi factor authentication level changed.
     *
     * @param event The event.
     */
    void onServerChangeMultiFactorAuthenticationLevel(ServerChangeMultiFactorAuthenticationLevelEvent event);
}
