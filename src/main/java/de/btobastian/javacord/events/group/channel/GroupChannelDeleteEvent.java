package de.btobastian.javacord.events.group.channel;

import de.btobastian.javacord.entities.channels.GroupChannel;

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
