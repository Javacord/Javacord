package org.javacord.event.channel.group.impl;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.event.channel.group.GroupChannelCreateEvent;

/**
 * The implementation of {@link GroupChannelCreateEvent}.
 */
public class ImplGroupChannelCreateEvent extends ImplGroupChannelEvent implements GroupChannelCreateEvent {

    /**
     * Creates a new group channel create event.
     *
     * @param channel The channel of the event.
     */
    public ImplGroupChannelCreateEvent(GroupChannel channel) {
        super(channel);
    }

}
