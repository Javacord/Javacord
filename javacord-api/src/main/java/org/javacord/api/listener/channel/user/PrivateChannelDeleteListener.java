package org.javacord.api.listener.channel.user;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.user.PrivateChannelDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to private channel deletions.
 */
@FunctionalInterface
@RequiredIntent({Intent.DIRECT_MESSAGES})
public interface PrivateChannelDeleteListener extends UserAttachableListener, PrivateChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a private channel is deleted.
     *
     * @param event The event.
     */
    void onPrivateChannelDelete(PrivateChannelDeleteEvent event);

}
