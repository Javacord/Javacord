package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangePendingEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user pending state changes (member screening pass).
 */
public interface UserChangePendingListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user's pending state changes.
     *
     * @param event The event.
     */
    void onServerMemberChangePending(UserChangePendingEvent event);

}
