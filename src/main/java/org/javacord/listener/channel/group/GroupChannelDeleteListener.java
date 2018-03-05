package org.javacord.listener.channel.group;

import org.javacord.event.channel.group.GroupChannelDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to group channel deletions.
 */
@FunctionalInterface
public interface GroupChannelDeleteListener extends UserAttachableListener, GroupChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a group channel is deleted.
     *
     * @param event The event.
     */
    void onGroupChannelDelete(GroupChannelDeleteEvent event);

}
