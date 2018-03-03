package org.javacord.listener.server.member;

import org.javacord.event.server.member.ServerMemberLeaveEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.server.ServerLeaveListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.server.member.ServerMemberLeaveEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.server.ServerLeaveListener;

/**
 * This listener listens to server member leaves.
 * Do not confuse it with the {@link ServerLeaveListener}:
 * ServerMemberRemoveListener is for other users and ServerLeaveListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberLeaveListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user leaves a server.
     *
     * @param event the event.
     */
    void onServerMemberLeave(ServerMemberLeaveEvent event);
}
