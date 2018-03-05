package org.javacord.listener.channel.server;

import org.javacord.event.channel.server.ServerChannelDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel deletions.
 */
@FunctionalInterface
public interface ServerChannelDeleteListener extends ServerAttachableListener, ServerChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server channel is deleted.
     *
     * @param event The event.
     */
    void onServerChannelDelete(ServerChannelDeleteEvent event);

}
