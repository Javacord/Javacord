package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerChangeOwnerEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server owner changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChangeOwnerListener extends ServerAttachableListener, GloballyAttachableListener,
                                                   ObjectAttachableListener {

    /**
     * This method is called every time a server's owner changed.
     *
     * @param event The event.
     */
    void onServerChangeOwner(ServerChangeOwnerEvent event);
}
