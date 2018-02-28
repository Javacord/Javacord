package de.btobastian.javacord.listener.channel.server;

import de.btobastian.javacord.event.channel.server.ServerChannelChangePositionEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
