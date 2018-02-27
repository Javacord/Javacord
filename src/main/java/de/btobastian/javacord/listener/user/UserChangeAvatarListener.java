package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.event.user.UserChangeAvatarEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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