package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberBanEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
