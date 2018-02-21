package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeActivityEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
