package org.javacord.api.listener.connection;

import org.javacord.api.event.connection.ReconnectEvent;
import org.javacord.api.listener.GloballyAttachableListener;

/**
 * This listener listens to reconnected sessions.
 * Reconnecting a session, means that it's likely that events were lost and therefore all objects were replaced.
 */
@FunctionalInterface
public interface ReconnectListener extends GloballyAttachableListener {

    /**
     * This method is called every time a connection is reconnected.
     *
     * @param event The event.
     */
    void onReconnect(ReconnectEvent event);

}
