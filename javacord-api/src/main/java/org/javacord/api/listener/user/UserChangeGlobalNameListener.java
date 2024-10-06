package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeGlobalNameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to global name changes.
 */
@FunctionalInterface
public interface UserChangeGlobalNameListener extends ServerAttachableListener, UserAttachableListener,
                                                        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their global name.
     *
     * @param event The event.
     */
    void onUserChangeGlobalName(UserChangeGlobalNameEvent event);

}
