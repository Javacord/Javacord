package de.btobastian.javacord.listener.channel.group;

import de.btobastian.javacord.event.channel.group.GroupChannelDeleteEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
