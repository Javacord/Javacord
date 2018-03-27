package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeNameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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