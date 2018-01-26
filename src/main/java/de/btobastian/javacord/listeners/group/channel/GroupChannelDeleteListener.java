package de.btobastian.javacord.listeners.group.channel;

import de.btobastian.javacord.events.group.channel.GroupChannelDeleteEvent;

/**
 * This listener listens to group channel deletions.
 */
@FunctionalInterface
public interface GroupChannelDeleteListener {

    /**
     * This method is called every time a group channel is deleted.
     *
     * @param event The event.
     */
    void onGroupChannelDelete(GroupChannelDeleteEvent event);

}
