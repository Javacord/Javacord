package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberChangeSelfMutedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to member self-muted changes.
 */
@FunctionalInterface
public interface ServerMemberChangeSelfMutedListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member changed their self-muted state on a server.
     *
     * @param event The event.
     */
    void onUserChangeSelfMuted(ServerMemberChangeSelfMutedEvent event);

}
