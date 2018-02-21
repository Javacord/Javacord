package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelCreateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
