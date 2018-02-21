package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeNameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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