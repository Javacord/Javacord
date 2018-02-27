package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

/**
 * This listener listens to server default message notification level changes.
 */
@FunctionalInterface
public interface ServerChangeDefaultMessageNotificationLevelListener extends ServerAttachableListener,
                                                                             GloballyAttachableListener,
                                                                             ObjectAttachableListener {

    /**
     * This method is called every time a server's default message notification level changed.
     *
     * @param event The event.
     */
    void onServerChangeDefaultMessageNotificationLevel(ServerChangeDefaultMessageNotificationLevelEvent event);
}
