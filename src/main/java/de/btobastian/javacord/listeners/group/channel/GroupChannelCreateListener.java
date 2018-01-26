package de.btobastian.javacord.listeners.group.channel;

import de.btobastian.javacord.events.group.channel.GroupChannelCreateEvent;

/**
 * This listener listens to group channel creations.
 */
@FunctionalInterface
public interface GroupChannelCreateListener {

    /**
     * This method is called every time a group channel is created.
     *
     * @param event The event.
     */
    void onGroupChannelCreate(GroupChannelCreateEvent event);
}
