package de.btobastian.javacord.event.channel.group;

import de.btobastian.javacord.entity.channel.GroupChannel;

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
