package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberUnbanEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

/**
 * This listener listens to server member unbans.
 */
@FunctionalInterface
public interface ServerMemberUnbanListener extends ServerAttachableListener, UserAttachableListener,
                                                   GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user got unbanned from a server.
     *
     * @param event The event.
     */
    void onServerMemberUnban(ServerMemberUnbanEvent event);
}
