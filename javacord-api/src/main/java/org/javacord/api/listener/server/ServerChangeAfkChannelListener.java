package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerChangeAfkChannelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server afk channel changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChangeAfkChannelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's afk channel changed.
     *
     * @param event The event.
     */
    void onServerChangeAfkChannel(ServerChangeAfkChannelEvent event);
}
