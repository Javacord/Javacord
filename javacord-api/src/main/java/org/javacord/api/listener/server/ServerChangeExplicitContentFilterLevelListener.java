package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server explicit content filter level changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChangeExplicitContentFilterLevelListener extends ServerAttachableListener,
        GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's explicit content filter level changed.
     *
     * @param event The event.
     */
    void onServerChangeExplicitContentFilterLevel(ServerChangeExplicitContentFilterLevelEvent event);
}
