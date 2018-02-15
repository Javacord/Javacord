package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelDeleteEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
