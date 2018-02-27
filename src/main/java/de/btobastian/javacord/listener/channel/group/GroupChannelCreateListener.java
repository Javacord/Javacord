package de.btobastian.javacord.listener.channel.group;

import de.btobastian.javacord.event.channel.group.GroupChannelCreateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
