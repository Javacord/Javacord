package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeServerAvatarEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user server avatar changes.
 */
@FunctionalInterface
public interface UserChangeServerAvatarListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changes their server avatar.
     *
     * @param event The event.
     */
    void onUserChangeServerAvatar(UserChangeServerAvatarEvent event);

}
