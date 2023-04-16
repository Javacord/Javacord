package org.javacord.api.listener.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.user.UserChangeDeafenedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to user deafened changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_VOICE_STATES})
public interface UserChangeDeafenedListener extends ServerAttachableListener, UserAttachableListener,
                                                    GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time the deafened state of a user is changed on a server.
     *
     * @param event The event.
     */
    void onUserChangeDeafened(UserChangeDeafenedEvent event);

}
