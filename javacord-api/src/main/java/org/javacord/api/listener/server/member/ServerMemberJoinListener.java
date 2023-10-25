package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to server member joins.
 * Do not confuse it with the {@link ServerJoinListener}:
 * {@code ServerMemberJoinListener} is for other users and {@code ServerJoinListener} is for yourself!
 */
@FunctionalInterface
public interface ServerMemberJoinListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member joins a server.
     *
     * @param event The event.
     */
    void onServerMemberJoin(ServerMemberJoinEvent event);
}
