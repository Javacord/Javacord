package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberChangePendingEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to member pending state changes (member screening pass).
 */
public interface ServerMemberChangePendingListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member's pending state changes.
     *
     * @param event The event.
     */
    void onServerMemberChangePending(ServerMemberChangePendingEvent event);

}
