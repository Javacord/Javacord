package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeInvitableEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server thread channel change invitable events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeInvitableListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread channel changes its invitable state.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeInvitable(ServerThreadChannelChangeInvitableEvent event);
}
