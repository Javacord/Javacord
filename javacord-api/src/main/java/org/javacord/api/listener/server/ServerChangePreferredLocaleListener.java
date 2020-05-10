package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangePreferredLocaleEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server preferred locale changes.
 */
@FunctionalInterface
public interface ServerChangePreferredLocaleListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's preferred locale changed.
     *
     * @param event The event.
     */
    void onServerChangePreferredLocale(ServerChangePreferredLocaleEvent event);
}
