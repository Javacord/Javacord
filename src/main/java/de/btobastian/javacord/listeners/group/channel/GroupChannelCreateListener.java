package de.btobastian.javacord.listeners.group.channel;

import de.btobastian.javacord.events.group.channel.GroupChannelCreateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
