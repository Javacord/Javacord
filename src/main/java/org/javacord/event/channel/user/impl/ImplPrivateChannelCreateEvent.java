package org.javacord.event.channel.user.impl;

import org.javacord.entity.channel.PrivateChannel;
import org.javacord.event.channel.user.PrivateChannelCreateEvent;

/**
 * The implementation of {@link PrivateChannelCreateEvent}.
 */
public class ImplPrivateChannelCreateEvent extends ImplPrivateChannelEvent implements PrivateChannelCreateEvent {

    /**
     * Creates a new private channel create event.
     *
     * @param channel The channel of the event.
     */
    public ImplPrivateChannelCreateEvent(PrivateChannel channel) {
        super(channel);
    }

}
