package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeTimeoutEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user timeout changes.
 */
@FunctionalInterface
public interface UserChangeTimeoutListener extends ServerAttachableListener, UserAttachableListener,
                                                    GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a timeout of a user changed on a server.
     *
     * @param event The event.
     */
    void onUserChangeTimeout(UserChangeTimeoutEvent event);
}
