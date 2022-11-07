package org.javacord.api.listener.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.user.UserChangeSelfDeafenedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to user self-deafened changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_VOICE_STATES})
public interface UserChangeSelfDeafenedListener extends ServerAttachableListener, UserAttachableListener,
                                                        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their self-deafened state on a server.
     *
     * @param event The event.
     */
    void onUserChangeSelfDeafened(UserChangeSelfDeafenedEvent event);

}
