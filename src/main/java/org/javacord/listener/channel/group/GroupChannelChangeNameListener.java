package org.javacord.listener.channel.group;

import org.javacord.event.channel.group.GroupChannelChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.channel.group.GroupChannelChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;

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
