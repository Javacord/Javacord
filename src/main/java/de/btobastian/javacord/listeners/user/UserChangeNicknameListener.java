package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeNicknameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
