package org.javacord.core.event.channel.user;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.channel.user.PrivateChannelEvent;
import org.javacord.core.event.user.UserEventImpl;

/**
 * The implementation of {@link PrivateChannelEvent}.
 */
public abstract class PrivateChannelEventImpl extends UserEventImpl implements PrivateChannelEvent {

    /**
     * The channel of the event.
     */
    private final PrivateChannel channel;

    /**
     * Creates a new private channel event.
     *
     * @param channel The channel of the event.
     */
    public PrivateChannelEventImpl(PrivateChannel channel) {
        super(channel.getRecipient());
        this.channel = channel;
    }

    @Override
    public PrivateChannel getChannel() {
        return channel;
    }

}
