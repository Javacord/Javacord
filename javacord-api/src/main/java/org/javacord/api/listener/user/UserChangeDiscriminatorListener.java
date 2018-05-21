package org.javacord.api.listener.user;

import org.javacord.api.event.user.UserChangeDiscriminatorEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to user discriminator changes.
 */
@FunctionalInterface
public interface UserChangeDiscriminatorListener extends ServerAttachableListener, UserAttachableListener,
                                                         GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user changed their discriminator.
     *
     * @param event The event.
     */
    void onUserChangeDiscriminator(UserChangeDiscriminatorEvent event);

}