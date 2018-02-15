package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeStatusEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
