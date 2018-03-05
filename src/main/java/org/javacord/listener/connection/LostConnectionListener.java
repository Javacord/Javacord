package org.javacord.listener.connection;

import org.javacord.event.connection.LostConnectionEvent;
import org.javacord.listener.GloballyAttachableListener;

/**
 * This listener listens to lost connections.
 * This listener is called, when the websocket loses its connection.
 * Don't panic! It's totally normal for the websocket to occasionally lose the connection. In most cases it's possible
 * to resume the session without missing any events!
 */
@FunctionalInterface
public interface LostConnectionListener extends GloballyAttachableListener {

    /**
     * This method is called every time a connection is lost.
     *
     * @param event The event.
     */
    void onLostConnection(LostConnectionEvent event);

}
