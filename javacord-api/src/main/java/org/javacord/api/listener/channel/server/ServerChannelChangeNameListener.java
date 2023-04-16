package org.javacord.api.listener.channel.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server channel name changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChannelChangeNameListener extends ServerAttachableListener, ServerChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server channel's name changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeName(ServerChannelChangeNameEvent event);
}
