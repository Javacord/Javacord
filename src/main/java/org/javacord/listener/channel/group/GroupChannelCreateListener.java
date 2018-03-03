package org.javacord.listener.channel.group;

import org.javacord.event.channel.group.GroupChannelCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.channel.group.GroupChannelCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
