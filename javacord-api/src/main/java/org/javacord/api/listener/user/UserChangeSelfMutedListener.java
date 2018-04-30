package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeSelfMutedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user self-muted changes.
 */
@FunctionalInterface
public interface UserChangeSelfMutedListener extends ServerAttachableListener, UserAttachableListener,
                                                     GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their self-muted state on a server.
     *
     * @param event The event.
     */
    void onUserChangeSelfMuted(UserChangeSelfMutedEvent event);

}
