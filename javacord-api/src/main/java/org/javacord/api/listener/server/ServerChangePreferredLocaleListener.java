package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerChangePreferredLocaleEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server preferred locale changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChangePreferredLocaleListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's preferred locale changed.
     *
     * @param event The event.
     */
    void onServerChangePreferredLocale(ServerChangePreferredLocaleEvent event);
}
