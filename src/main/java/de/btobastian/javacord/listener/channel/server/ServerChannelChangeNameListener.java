package de.btobastian.javacord.listener.channel.server;

import de.btobastian.javacord.event.channel.server.ServerChannelChangeNameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
