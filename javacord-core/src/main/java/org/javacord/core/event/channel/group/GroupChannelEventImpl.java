package org.javacord.core.event.channel.group;

import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.event.channel.group.GroupChannelEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link GroupChannelEvent}.
 */
public abstract class GroupChannelEventImpl extends EventImpl implements GroupChannelEvent {

    /**
     * The channel of the event.
     */
    private final GroupChannel channel;

    /**
     * Creates a new group channel event.
     *
     * @param channel The channel of the event.
     */
    public GroupChannelEventImpl(GroupChannel channel) {
        super(channel.getApi());
        this.channel = channel;
    }

    @Override
    public GroupChannel getChannel() {
        return channel;
    }

}
