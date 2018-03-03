package org.javacord.listener.user;

import org.javacord.event.user.UserChangeStatusEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to user status changes.
 */
@FunctionalInterface
public interface UserChangeStatusListener extends ServerAttachableListener, UserAttachableListener,
                                                  GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their status.
     *
     * @param event The event.
     */
    void onUserChangeStatus(UserChangeStatusEvent event);
}
