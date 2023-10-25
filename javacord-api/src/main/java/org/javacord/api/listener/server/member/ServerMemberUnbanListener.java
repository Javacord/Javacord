package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberUnbanEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to server member unbans.
 */
@FunctionalInterface
public interface ServerMemberUnbanListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member got unbanned from a server.
     *
     * @param event The event.
     */
    void onServerMemberUnban(ServerMemberUnbanEvent event);
}
