package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerPrivateThreadJoinEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to private thread joins.
 */
@FunctionalInterface
public interface ServerPrivateThreadJoinListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time you (the bot) joins a server thread channel.
     *
     * @param event The event.
     */
    void onServerPrivateThreadJoin(ServerPrivateThreadJoinEvent event);
}
