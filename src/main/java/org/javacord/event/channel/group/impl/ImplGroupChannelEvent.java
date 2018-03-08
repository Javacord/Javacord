package org.javacord.event.channel.group.impl;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.event.Event;
import org.javacord.event.channel.group.GroupChannelEvent;
import org.javacord.event.impl.ImplEvent;

/**
 * The implementation of {@link GroupChannelEvent}.
 */
public abstract class ImplGroupChannelEvent extends ImplEvent implements GroupChannelEvent {

    /**
     * The channel of the event.
     */
    private final GroupChannel channel;

    /**
     * Creates a new group channel event.
     *
     * @param channel The channel of the event.
     */
    public ImplGroupChannelEvent(GroupChannel channel) {
        super(channel.getApi());
        this.channel = channel;
    }

    @Override
    public GroupChannel getChannel() {
        return channel;
    }

}
