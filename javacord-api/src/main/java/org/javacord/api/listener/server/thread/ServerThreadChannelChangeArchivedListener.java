package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchivedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server thread channel archived change events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeArchivedListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread's archived state changes.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeArchived(ServerThreadChannelChangeArchivedEvent event);
}
