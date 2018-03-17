package org.javacord.api.listener.channel.group;

import org.javacord.api.event.channel.group.GroupChannelChangeNameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to group channel name changes.
 */
@FunctionalInterface
public interface GroupChannelChangeNameListener extends UserAttachableListener, GroupChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a group channel's name changes.
     *
     * @param event The event.
     */
    void onGroupChannelChangeName(GroupChannelChangeNameEvent event);
}
