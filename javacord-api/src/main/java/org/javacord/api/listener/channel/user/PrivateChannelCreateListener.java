package org.javacord.api.listener.channel.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.user.PrivateChannelCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to private channel creations.
 */
@FunctionalInterface
@RequiredIntent({Intent.DIRECT_MESSAGES})
public interface PrivateChannelCreateListener extends UserAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a private channel is created.
     *
     * @param event The event.
     */
    void onPrivateChannelCreate(PrivateChannelCreateEvent event);
}
