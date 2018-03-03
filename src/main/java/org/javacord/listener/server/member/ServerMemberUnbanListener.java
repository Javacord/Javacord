package org.javacord.listener.server.member;

import org.javacord.event.server.member.ServerMemberUnbanEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.server.member.ServerMemberUnbanEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
