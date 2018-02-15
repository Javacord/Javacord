package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangePositionEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
