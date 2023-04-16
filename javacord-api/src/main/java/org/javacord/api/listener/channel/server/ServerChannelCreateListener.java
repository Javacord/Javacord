package org.javacord.api.listener.channel.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.ServerChannelCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server channel creations.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChannelCreateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel is created.
     *
     * @param event The event.
     */
    void onServerChannelCreate(ServerChannelCreateEvent event);
}
