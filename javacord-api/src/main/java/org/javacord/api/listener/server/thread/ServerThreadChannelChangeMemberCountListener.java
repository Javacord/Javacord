package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMemberCountEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server thread channel member count change events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeMemberCountListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread's member count changes.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeMemberCount(ServerThreadChannelChangeMemberCountEvent event);
}
