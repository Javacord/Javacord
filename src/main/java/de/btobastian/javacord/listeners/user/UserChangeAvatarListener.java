package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeAvatarEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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