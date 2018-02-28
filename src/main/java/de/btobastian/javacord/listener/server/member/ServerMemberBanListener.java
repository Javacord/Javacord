package de.btobastian.javacord.listener.server.member;

import de.btobastian.javacord.event.server.member.ServerMemberBanEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
