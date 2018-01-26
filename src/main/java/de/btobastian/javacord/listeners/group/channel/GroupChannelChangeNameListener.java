package de.btobastian.javacord.listeners.group.channel;

import de.btobastian.javacord.events.group.channel.GroupChannelChangeNameEvent;

/**
 * This listener listens to group channel name changes.
 */
@FunctionalInterface
public interface GroupChannelChangeNameListener {

    /**
     * This method is called every time a group channel's name changes.
     *
     * @param event The event.
     */
    void onGroupChannelChangeName(GroupChannelChangeNameEvent event);
}
