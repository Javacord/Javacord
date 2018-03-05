package org.javacord.listener.channel.server;

import org.javacord.event.channel.server.ServerChannelChangePositionEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel position changes.
 */
@FunctionalInterface
public interface ServerChannelChangePositionListener extends ServerAttachableListener, ServerChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server channel's position changes.
     *
     * @param event The event.
     */
    void onServerChannelChangePosition(ServerChannelChangePositionEvent event);
}
