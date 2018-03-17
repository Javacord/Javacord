package org.javacord.core.event.channel.user;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.channel.user.PrivateChannelDeleteEvent;

/**
 * The implementation of {@link PrivateChannelDeleteEvent}.
 */
public class PrivateChannelDeleteEventImpl extends PrivateChannelEventImpl implements PrivateChannelDeleteEvent {

    /**
     * Creates a new private channel delete event.
     *
     * @param channel The channel of the event.
     */
    public PrivateChannelDeleteEventImpl(PrivateChannel channel) {
        super(channel);
    }

}
