package org.javacord.api.listener.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.user.UserChangeAvatarEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to user avatar changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MEMBERS})
public interface UserChangeAvatarListener extends ServerAttachableListener, UserAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their avatar.
     *
     * @param event The event.
     */
    void onUserChangeAvatar(UserChangeAvatarEvent event);

}