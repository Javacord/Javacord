package org.javacord.event.channel.user.impl;

import org.javacord.entity.channel.PrivateChannel;
import org.javacord.event.channel.user.PrivateChannelEvent;
import org.javacord.event.user.impl.ImplUserEvent;

/**
 * The implementation of {@link PrivateChannelEvent}.
 */
public abstract class ImplPrivateChannelEvent extends ImplUserEvent implements PrivateChannelEvent {

    /**
     * The channel of the event.
     */
    private final PrivateChannel channel;

    /**
     * Creates a new private channel event.
     *
     * @param channel The channel of the event.
     */
    public ImplPrivateChannelEvent(PrivateChannel channel) {
        super(channel.getRecipient());
        this.channel = channel;
    }

    @Override
    public PrivateChannel getChannel() {
        return channel;
    }

}
