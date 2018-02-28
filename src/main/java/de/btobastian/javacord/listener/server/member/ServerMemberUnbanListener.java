package de.btobastian.javacord.listener.server.member;

import de.btobastian.javacord.event.server.member.ServerMemberUnbanEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
