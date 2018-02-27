package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.event.user.UserChangeActivityEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to user activity changes.
 */
@FunctionalInterface
public interface UserChangeActivityListener extends ServerAttachableListener, UserAttachableListener,
                                                    GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their activity.
     *
     * @param event The event
     */
    void onUserChangeActivity(UserChangeActivityEvent event);
}
