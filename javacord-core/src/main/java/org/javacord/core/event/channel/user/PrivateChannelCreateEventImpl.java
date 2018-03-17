package org.javacord.core.event.channel.user;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.channel.user.PrivateChannelCreateEvent;

/**
 * The implementation of {@link PrivateChannelCreateEvent}.
 */
public class PrivateChannelCreateEventImpl extends PrivateChannelEventImpl implements PrivateChannelCreateEvent {

    /**
     * Creates a new private channel create event.
     *
     * @param channel The channel of the event.
     */
    public PrivateChannelCreateEventImpl(PrivateChannel channel) {
        super(channel);
    }

}
