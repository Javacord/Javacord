package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberBanEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to server member bans.
 */
@FunctionalInterface
public interface ServerMemberBanListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member got banned from a server.
     *
     * @param event The event.
     */
    void onServerMemberBan(ServerMemberBanEvent event);
}
