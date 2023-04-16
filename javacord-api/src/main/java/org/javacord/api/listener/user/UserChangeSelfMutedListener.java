package org.javacord.api.listener.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.user.UserChangeSelfMutedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to user self-muted changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_VOICE_STATES})
public interface UserChangeSelfMutedListener extends ServerAttachableListener, UserAttachableListener,
                                                     GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their self-muted state on a server.
     *
     * @param event The event.
     */
    void onUserChangeSelfMuted(UserChangeSelfMutedEvent event);

}
