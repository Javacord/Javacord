package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeNicknameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
