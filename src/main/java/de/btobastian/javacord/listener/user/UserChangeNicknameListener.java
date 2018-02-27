package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.event.user.UserChangeNicknameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to user nickname changes.
 */
@FunctionalInterface
public interface UserChangeNicknameListener extends ServerAttachableListener, UserAttachableListener,
                                                    GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their nickname on a server.
     *
     * @param event The event.
     */
    void onUserChangeNickname(UserChangeNicknameEvent event);
}
