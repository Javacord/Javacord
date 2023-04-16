package org.javacord.api.listener.server.member;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.member.ServerMemberBanEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server member bans.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MODERATION})
public interface ServerMemberBanListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user got banned from a server.
     *
     * @param event The event.
     */
    void onServerMemberBan(ServerMemberBanEvent event);
}
