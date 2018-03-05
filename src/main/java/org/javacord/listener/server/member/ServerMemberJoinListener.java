package org.javacord.listener.server.member;

import org.javacord.event.server.member.ServerMemberJoinEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.server.ServerJoinListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to server member joins.
 * Do not confuse it with the {@link ServerJoinListener}:
 * ServerMemberAddListener is for other users and ServerJoinListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberJoinListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user joins a server.
     *
     * @param event The event.
     */
    void onServerMemberJoin(ServerMemberJoinEvent event);
}
