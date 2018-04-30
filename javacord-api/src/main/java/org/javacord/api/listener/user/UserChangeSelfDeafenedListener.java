package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeSelfDeafenedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user self-deafened changes.
 */
@FunctionalInterface
public interface UserChangeSelfDeafenedListener extends ServerAttachableListener, UserAttachableListener,
                                                        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their self-deafened state on a server.
     *
     * @param event The event.
     */
    void onUserChangeSelfDeafened(UserChangeSelfDeafenedEvent event);

}
