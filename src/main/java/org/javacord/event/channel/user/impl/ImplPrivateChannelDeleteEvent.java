package org.javacord.event.channel.user.impl;

import org.javacord.entity.channel.PrivateChannel;
import org.javacord.event.channel.user.PrivateChannelDeleteEvent;

/**
 * The implementation of {@link PrivateChannelDeleteEvent}.
 */
public class ImplPrivateChannelDeleteEvent extends ImplPrivateChannelEvent implements PrivateChannelDeleteEvent {

    /**
     * Creates a new private channel delete event.
     *
     * @param channel The channel of the event.
     */
    public ImplPrivateChannelDeleteEvent(PrivateChannel channel) {
        super(channel);
    }

}
