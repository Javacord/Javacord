package de.btobastian.javacord.listeners.group.channel;

import de.btobastian.javacord.events.group.channel.GroupChannelChangeNameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
