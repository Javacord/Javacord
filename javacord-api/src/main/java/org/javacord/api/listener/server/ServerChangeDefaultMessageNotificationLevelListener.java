package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

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
