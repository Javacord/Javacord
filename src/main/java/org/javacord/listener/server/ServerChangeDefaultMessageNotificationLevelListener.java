package org.javacord.listener.server;

import org.javacord.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
