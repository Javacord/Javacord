package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeStatusEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
