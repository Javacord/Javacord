package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangeNameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
