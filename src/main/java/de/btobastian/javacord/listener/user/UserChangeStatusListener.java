package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.event.user.UserChangeStatusEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
