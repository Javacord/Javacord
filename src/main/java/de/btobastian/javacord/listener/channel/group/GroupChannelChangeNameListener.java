package de.btobastian.javacord.listener.channel.group;

import de.btobastian.javacord.event.channel.group.GroupChannelChangeNameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
