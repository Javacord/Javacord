package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberChangeDeafenedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to member deafened changes.
 */
@FunctionalInterface
public interface ServerMemberChangeDeafenedListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time the deafened state of a member is changed on a server.
     *
     * @param event The event.
     */
    void onUserChangeDeafened(ServerMemberChangeDeafenedEvent event);

}
