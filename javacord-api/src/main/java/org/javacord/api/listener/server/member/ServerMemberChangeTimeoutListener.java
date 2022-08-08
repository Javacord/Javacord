package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberChangeTimeoutEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to member timeout changes.
 */
@FunctionalInterface
public interface ServerMemberChangeTimeoutListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a timeout of a member changed on a server.
     *
     * @param event The event.
     */
    void onUserChangeTimeout(ServerMemberChangeTimeoutEvent event);
}
