package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeActivityEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
