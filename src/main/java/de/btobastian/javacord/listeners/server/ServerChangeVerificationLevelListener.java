package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeVerificationLevelEvent;

/**
 * This listener listens to server verification level changes.
 */
@FunctionalInterface
public interface ServerChangeVerificationLevelListener {

    /**
     * This method is called every time a server's verification level changed.
     *
     * @param event The event.
     */
    void onServerChangeVerificationLevel(ServerChangeVerificationLevelEvent event);
}
