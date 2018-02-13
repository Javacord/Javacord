package de.btobastian.javacord.events.group.channel;

import de.btobastian.javacord.entities.channels.GroupChannel;

/**
 * A group channel create event.
 */
public class GroupChannelCreateEvent extends GroupChannelEvent {

    /**
     * Creates a new group channel create event.
     *
     * @param channel The channel of the event.
     */
    public GroupChannelCreateEvent(GroupChannel channel) {
        super(channel);
    }

}
