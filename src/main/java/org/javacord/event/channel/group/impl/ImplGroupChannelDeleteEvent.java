package org.javacord.event.channel.group.impl;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.event.channel.group.GroupChannelDeleteEvent;

/**
 * The implementation of {@link GroupChannelDeleteEvent}.
 */
public class ImplGroupChannelDeleteEvent extends ImplGroupChannelEvent implements GroupChannelDeleteEvent {

    /**
     * Creates a new group channel delete event.
     *
     * @param channel The channel of the event.
     */
    public ImplGroupChannelDeleteEvent(GroupChannel channel) {
        super(channel);
    }

}
