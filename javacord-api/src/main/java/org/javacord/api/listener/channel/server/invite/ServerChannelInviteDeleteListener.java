package org.javacord.api.listener.channel.server.invite;

import org.javacord.api.event.channel.server.invite.ServerChannelInviteDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel invite deletions.
 */
@FunctionalInterface
public interface ServerChannelInviteDeleteListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel invite is deleted.
     *
     * @param event The event.
     */
    void onServerChannelInviteDelete(ServerChannelInviteDeleteEvent event);
}
