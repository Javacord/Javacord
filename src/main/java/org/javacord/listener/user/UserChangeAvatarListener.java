package org.javacord.listener.user;

import org.javacord.event.user.UserChangeAvatarEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to user avatar changes.
 */
@FunctionalInterface
public interface UserChangeAvatarListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their avatar.
     *
     * @param event The event.
     */
    void onUserChangeAvatar(UserChangeAvatarEvent event);

}