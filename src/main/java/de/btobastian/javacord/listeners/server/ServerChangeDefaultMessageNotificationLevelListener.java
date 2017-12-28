package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerChangeDefaultMessageNotificationLevelEvent;

/**
 * This listener listens to server default message notification level changes.
 */
@FunctionalInterface
public interface ServerChangeDefaultMessageNotificationLevelListener {

    /**
     * This method is called every time a server's default message notification level changed.
     *
     * @param event The event.
     */
    void onServerChangeDefaultMessageNotificationLevel(ServerChangeDefaultMessageNotificationLevelEvent event);
}
