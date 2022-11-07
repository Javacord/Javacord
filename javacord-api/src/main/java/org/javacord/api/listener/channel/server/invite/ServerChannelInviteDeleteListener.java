package org.javacord.api.listener.channel.server.invite;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.invite.ServerChannelInviteDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server channel invite deletions.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_INVITES})
public interface ServerChannelInviteDeleteListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel invite is deleted.
     *
     * @param event The event.
     */
    void onServerChannelInviteDelete(ServerChannelInviteDeleteEvent event);
}
