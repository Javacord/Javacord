package org.javacord.api.listener.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.user.UserChangeNicknameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to user nickname changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MEMBERS})
public interface UserChangeNicknameListener extends ServerAttachableListener, UserAttachableListener,
                                                    GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their nickname on a server.
     *
     * @param event The event.
     */
    void onUserChangeNickname(UserChangeNicknameEvent event);
}
