package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeTotalMessageSentEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server thread channel total messages change events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeTotalMessageSentListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread's total messages changes.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeTotalMessageSent(ServerThreadChannelChangeTotalMessageSentEvent event);
}
