package org.javacord.listener.channel.server;

import org.javacord.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel name changes.
 */
@FunctionalInterface
public interface ServerChannelChangeNameListener extends ServerAttachableListener, ServerChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server channel's name changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeName(ServerChannelChangeNameEvent event);
}
