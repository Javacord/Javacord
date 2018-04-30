package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeMutedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user muted changes.
 */
@FunctionalInterface
public interface UserChangeMutedListener extends ServerAttachableListener, UserAttachableListener,
                                                 GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time the muted state of user is changed on a server.
     *
     * @param event The event.
     */
    void onUserChangeMuted(UserChangeMutedEvent event);

}
