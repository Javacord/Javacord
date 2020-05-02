package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeRulesChannelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server rules channel changes.
 */
@FunctionalInterface
public interface ServerChangeRulesChannelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's rules channel changed.
     *
     * @param event The event.
     */
    void onServerChangeRulesChannel(ServerChangeRulesChannelEvent event);
}
