package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberChangeActivityEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to server member activity changes.
 */
@FunctionalInterface
public interface ServerMemberChangeActivityListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server member changed their activity.
     *
     * @param event The event
     */
    void onServerMemberChangeActivity(ServerMemberChangeActivityEvent event);
}
