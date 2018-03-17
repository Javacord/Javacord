package org.javacord.api.listener.channel.group;

import org.javacord.api.event.channel.group.GroupChannelCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to group channel creations.
 */
@FunctionalInterface
public interface GroupChannelCreateListener extends UserAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a group channel is created.
     *
     * @param event The event.
     */
    void onGroupChannelCreate(GroupChannelCreateEvent event);
}
