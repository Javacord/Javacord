package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.ServerLeaveListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to server member leaves.
 * Do not confuse it with the {@link ServerLeaveListener}:
 * {@code ServerMemberLeaveListener} is for other users and {@code ServerLeaveListener} is for yourself!
 */
@FunctionalInterface
public interface ServerMemberLeaveListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member leaves a server.
     *
     * @param event the event.
     */
    void onServerMemberLeave(ServerMemberLeaveEvent event);
}
