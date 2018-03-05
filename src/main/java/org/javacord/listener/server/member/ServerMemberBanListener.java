package org.javacord.listener.server.member;

import org.javacord.event.server.member.ServerMemberBanEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to server member bans.
 */
@FunctionalInterface
public interface ServerMemberBanListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user got banned from a server.
     *
     * @param event The event.
     */
    void onServerMemberBan(ServerMemberBanEvent event);
}
