package de.btobastian.javacord.listeners.group.channel;

import de.btobastian.javacord.events.group.channel.GroupChannelDeleteEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
