package org.javacord.api.listener.channel.server;

import org.javacord.api.event.channel.server.ServerChannelCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel creations.
 */
@FunctionalInterface
public interface ServerChannelCreateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel is created.
     *
     * @param event The event.
     */
    void onServerChannelCreate(ServerChannelCreateEvent event);
}
