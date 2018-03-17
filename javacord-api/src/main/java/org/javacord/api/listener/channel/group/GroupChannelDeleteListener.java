package org.javacord.api.listener.channel.group;

import org.javacord.api.event.channel.group.GroupChannelDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

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
