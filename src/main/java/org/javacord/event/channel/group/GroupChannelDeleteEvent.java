package org.javacord.event.channel.group;

import org.javacord.entity.channel.GroupChannel;

/**
 * A group channel delete event.
 */
public class GroupChannelDeleteEvent extends GroupChannelEvent {

    /**
     * Creates a new group channel delete event.
     *
     * @param channel The channel of the event.
     */
    public GroupChannelDeleteEvent(GroupChannel channel) {
        super(channel);
    }

}
