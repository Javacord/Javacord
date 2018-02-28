package de.btobastian.javacord.listener.channel.server;

import de.btobastian.javacord.event.channel.server.ServerChannelCreateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
