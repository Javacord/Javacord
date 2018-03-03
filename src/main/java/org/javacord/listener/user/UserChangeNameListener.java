package org.javacord.listener.user;

import org.javacord.event.user.UserChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.user.UserChangeNameEvent;

/**
 * This listener listens to user name changes.
 */
@FunctionalInterface
public interface UserChangeNameListener extends ServerAttachableListener, UserAttachableListener,
                                                GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their name.
     *
     * @param event The event.
     */
    void onUserChangeName(UserChangeNameEvent event);

}