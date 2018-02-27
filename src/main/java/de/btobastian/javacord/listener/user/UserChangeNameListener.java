package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.event.user.UserChangeNameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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