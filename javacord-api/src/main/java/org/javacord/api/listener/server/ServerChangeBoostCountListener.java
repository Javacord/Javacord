package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerChangeBoostCountEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server boost count changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChangeBoostCountListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {
    /**
     * This method is called every time a server's boost count changed.
     *
     * @param event The event.
     */
    void onServerChangeBoostCount(ServerChangeBoostCountEvent event);
}
