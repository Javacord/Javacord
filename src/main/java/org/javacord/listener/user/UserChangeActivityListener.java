package org.javacord.listener.user;

import org.javacord.event.user.UserChangeActivityEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
