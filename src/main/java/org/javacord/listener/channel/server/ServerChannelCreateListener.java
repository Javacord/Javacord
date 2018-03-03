package org.javacord.listener.channel.server;

import org.javacord.event.channel.server.ServerChannelCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.channel.server.ServerChannelCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
