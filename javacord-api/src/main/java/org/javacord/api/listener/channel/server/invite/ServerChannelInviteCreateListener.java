package org.javacord.api.listener.channel.server.invite;

import org.javacord.api.event.channel.server.invite.ServerChannelInviteCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel invite creations.
 */
@FunctionalInterface
public interface ServerChannelInviteCreateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel invite is created.
     *
     * @param event The event.
     */
    void onServerChannelInviteCreate(ServerChannelInviteCreateEvent event);
}
