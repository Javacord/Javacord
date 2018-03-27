package org.javacord.api.listener.channel.server;

import org.javacord.api.event.channel.server.ServerChannelDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
